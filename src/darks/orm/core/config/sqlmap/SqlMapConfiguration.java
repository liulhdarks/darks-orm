/**
 * 
 * Copyright 2014 The Darks ORM Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package darks.orm.core.config.sqlmap;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import darks.orm.core.data.tags.AbstractTag;
import darks.orm.core.data.xml.DDLData;
import darks.orm.core.data.xml.DMLData;
import darks.orm.core.data.xml.DMLData.DMLType;
import darks.orm.core.data.xml.DMLQueryData;
import darks.orm.core.data.xml.DMLUpdateData;
import darks.orm.exceptions.ConfigException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.XmlHelper;

@SuppressWarnings("unchecked")
public class SqlMapConfiguration
{
    
    private static final Logger log = LoggerFactory.getLogger(SqlMapConfiguration.class);
    
    private static final String DTD_PUBLIC_ID = "-//darks//DTD sqlmap 3.0//EN";
    
    private static final String CONFIG_DTD_PATH = "/darks/orm/core/config/sqlmap.dtd";
    
    private ConcurrentMap<String, DDLData> ddlMap = new ConcurrentHashMap<String, DDLData>();
    
    private ConcurrentMap<String, DMLData> dmlMap = new ConcurrentHashMap<String, DMLData>();
    
    private ConcurrentMap<String, AbstractTag> tagsMap = new ConcurrentHashMap<String, AbstractTag>();
    
    private List<String> sqlMapCfgs;
    
    private DDLConfigReader ddlReader;
    
    private DMLConfigReader dmlReader;
    
    private String catalog;
    
    private String schema;
    
    public SqlMapConfiguration()
    {
        ddlReader = new DDLConfigReader(this);
        dmlReader = new DMLConfigReader(this);
    }
    
    public SqlMapConfiguration(List<String> sqlMapCfgs)
    {
        this.sqlMapCfgs = sqlMapCfgs;
        ddlReader = new DDLConfigReader(this);
        dmlReader = new DMLConfigReader(this);
    }
    
    public void addDDLData(String id, DDLData ddlData)
    {
        ddlMap.put(id, ddlData);
    }
    
    public void addDMLData(String id, DMLData dmlData)
    {
        dmlMap.put(id, dmlData);
    }
    
    public void addTag(String id, AbstractTag tag)
    {
        tagsMap.put(id, tag);
    }
    
    public AbstractTag getTag(String id)
    {
    	return tagsMap.get(id);
    }
    
    /**
     * Get {@linkplain darks.orm.core.data.xml.DMLData DMLData} by id
     * @param id DML data id
     */
    public DMLData getDMLData(String id)
    {
        if (dmlMap.containsKey(id))
        {
            return dmlMap.get(id);
        }
        return null;
    }
    
    /**
     * Load sqlmap config files by path list
     * 
     * @param sqlMapCfgs Path list
     * @throws Exception
     */
    public void loadSqlMap(List<String> sqlMapCfgs)
        throws DocumentException
    {
        this.sqlMapCfgs = sqlMapCfgs;
        for (String sqlMapCfg : sqlMapCfgs)
        {
            loadSqlMap(sqlMapCfg);
        }
    }
    
    /**
     * Load sqlmap config file by path
     * 
     * @param sqlMapCfg sqlmap config file path
     * @throws DocumentException
     */
    public void loadSqlMap(String sqlMapCfg)
        throws DocumentException
    {
        if (!sqlMapCfg.startsWith("/"))
            sqlMapCfg = "/" + sqlMapCfg;
        InputStream cfgIns = getClass().getResourceAsStream(sqlMapCfg);
        if (cfgIns == null)
        {
            log.error("[SQLMAP]'" + sqlMapCfg + "' configuration file does not exists.");
            return;
        }
        else
        {
            log.debug("[SQLMAP]Load '" + sqlMapCfg + "' sqlmap configuration file");
        }
        Document doc = XmlHelper.getXMLDocument(DTD_PUBLIC_ID, CONFIG_DTD_PATH, cfgIns);
        if (doc == null)
        {
        	throw new ConfigException("Fail to parse sqlmap config file. Cause invalid config file.");
        }
        Element root = doc.getRootElement();
        for (Iterator<Element> it = root.elementIterator(); it.hasNext();)
        {
            try
            {
                Element el = it.next();
                String name = el.getName().trim();
                if ("DDL".equalsIgnoreCase(name))
                {
                    ddlReader.reader(el);
                }
                else if ("DML".equalsIgnoreCase(name))
                {
                    dmlReader.reader(el);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * debug SQLMAP config info
     */
    public void outputSqlMapCfgInfo()
    {
        // DDL
        System.out.println("---<DDL>------------------------------");
        for (Entry<String, DDLData> entry : ddlMap.entrySet())
        {
            String key = entry.getKey();
            DDLData ddlData = entry.getValue();
            System.out.println("Key:" + key + " Id:" + ddlData.getId() + " type:" + ddlData.getType() + " sql:"
                + ddlData.getSql());
        }
        // DML
        System.out.println("---<DML>------------------------------");
        for (Entry<String, DMLData> entry : dmlMap.entrySet())
        {
            String key = entry.getKey();
            DMLData dmlData = entry.getValue();
            System.out.println("Key:" + key + " Id:" + dmlData.getId() + " type:" + dmlData.getType() + " ");
            if (dmlData.getType() == DMLType.Query)
            {
                DMLQueryData qd = dmlData.getQueryData();
                System.out.println("alias:" + qd.getAlias() + " attr:" + qd.getAttribute() + " rettype:"
                    + qd.getResultType() + " qdType" + qd.getQueryDataType() + " qType" + qd.getQueryType());
                Map<String, String> map = qd.getSqls();
                for (Entry<String, String> ent : map.entrySet())
                {
                    System.out.println("val:" + ent.getKey() + " sql:" + ent.getValue());
                }
            }
            else if (dmlData.getType() == DMLType.Update)
            {
                DMLUpdateData ud = dmlData.getUpdateData();
                System.out.println("sql:" + ud.getSql());
            }
            System.out.println("----------");
        }
    }
    
    public ConcurrentMap<String, DDLData> getDdlMap()
    {
        return ddlMap;
    }
    
    public void setDdlMap(ConcurrentMap<String, DDLData> ddlMap)
    {
        this.ddlMap = ddlMap;
    }
    
    public ConcurrentMap<String, DMLData> getDmlMap()
    {
        return dmlMap;
    }
    
    public void setDmlMap(ConcurrentMap<String, DMLData> dmlMap)
    {
        this.dmlMap = dmlMap;
    }
    
    public DDLConfigReader getDdlReader()
    {
        return ddlReader;
    }
    
    public void setDdlReader(DDLConfigReader ddlReader)
    {
        this.ddlReader = ddlReader;
    }
    
    public DMLConfigReader getDmlReader()
    {
        return dmlReader;
    }
    
    public void setDmlReader(DMLConfigReader dmlReader)
    {
        this.dmlReader = dmlReader;
    }
    
    public List<String> getSqlMapCfgs()
    {
        return sqlMapCfgs;
    }
    
    public void setSqlMapCfgs(List<String> sqlMapCfgs)
    {
        this.sqlMapCfgs = sqlMapCfgs;
    }
    
    public String getCatalog()
    {
        return catalog;
    }
    
    public void setCatalog(String catalog)
    {
        this.catalog = catalog;
    }
    
    public String getSchema()
    {
        return schema;
    }
    
    public void setSchema(String schema)
    {
        this.schema = schema;
    }
    
}