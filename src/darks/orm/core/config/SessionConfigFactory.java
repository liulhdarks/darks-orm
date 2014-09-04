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

package darks.orm.core.config;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import darks.orm.core.config.CacheConfiguration.CacheConfigType;
import darks.orm.exceptions.ClassReflectException;
import darks.orm.exceptions.ConfigException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.FileHelper;
import darks.orm.util.ReflectHelper;
import darks.orm.util.XmlHelper;

/**
 * Session config factory will build ORM config environment from config file.
 * @author darks
 */
public final class SessionConfigFactory
{
    
    private static final Logger logger = LoggerFactory.getLogger(SessionConfigFactory.class);
    
    public static final String DEFAULT_CONFIG_PATH = "/darks.xml";
    
    private static final String DTD_PUBLIC_ID = "-//darks//DTD darks 3.0//EN";
    
    private static final String CONFIG_DTD_PATH = "/darks/orm/core/config/darks.dtd";
    
    private static Map<String, Class<?>> configMap = new HashMap<String, Class<?>>(3);
    
    static
    {
        configMap.put("jdbc", JdbcConfiguration.class);
        configMap.put("bonecp", BoneCPConfiguration.class);
        configMap.put("jndi", JndiConfiguration.class);
    }
    
    private SessionConfigFactory()
    {
        
    }

    /**
     * Get config file entity from file.
     * 
     * @return Config file entity
     * @throws ConfigException
     */
    public static Configuration getConfiguration()
    				throws ConfigException
    {
    	return getConfiguration(DEFAULT_CONFIG_PATH);
    }
    
    /**
     * Get config file entity from file.
     * 
     * @param configLocation Config file location
     * @return Config file entity
     * @throws ConfigException
     */
    public static Configuration getConfiguration(String configLocation)
        throws ConfigException
    {
    	if (configLocation == null)
    	{
    		throw new ConfigException("ConfigLocation is null.");
    	}
    	InputStream cfgIns = SessionConfigFactory.class.getResourceAsStream(configLocation);
        if (cfgIns == null)
        {
            throw new ConfigException("'" + configLocation + "' configuration file does not exists.");
        }
        return getConfiguration(cfgIns);
    }
    
    /**
     * Get config file entity from file.
     * 
     * @param configPath Config file path
     * @return Config file entity
     * @throws ConfigException
     */
    public static Configuration getConfiguration(InputStream cfgIns)
        throws ConfigException
    {
        Configuration cfg = new Configuration();
        Document doc = XmlHelper.getXMLDocument(DTD_PUBLIC_ID, CONFIG_DTD_PATH, cfgIns);
        if (doc == null)
        {
        	throw new ConfigException("Fail to parse config file. Cause invalid config file.");
        }
        // Parse DataSource
        parseDataSourceXpath(doc, cfg);
        // Parse entity config
        parseEntityXpath(doc, cfg);
        // Parse SqlMap path config
        parseSqlMapXpath(doc, cfg);
        // Parse cache config
        parseCacheXpath(doc, cfg);
        return cfg;
    }
    
    /**
     * Parse DataSource config
     * 
     * @param doc Document
     * @param cfg Configuration config
     * @throws ConfigException
     */
    private static void parseDataSourceXpath(Document doc, Configuration cfg)
        throws ConfigException
    {
        String xpath = "/darks/dataSource[@type]";
        List<?> nodes = doc.selectNodes(xpath);
        Iterator<?> it = nodes.iterator();
        while (it.hasNext())
        {
            Element node = (Element)it.next();
            parseDataSourceNode(node, cfg);
        }
        cfg.ensureDataSourceChain();
        cfg.ensureMainDataSourceConfig();
    }
    
    /**
     * Parse DataSource single element
     * 
     * @param node XML element node
     * @param cfg Configuration config
     * @throws ConfigException
     */
    private static void parseDataSourceNode(Element node, Configuration cfg)
        throws ConfigException
    {
        DataSourceConfiguration dsConfig = null;
        Class<?> clazz = null;
        if (node == null)
        {
            throw new ConfigException("document does not has 'dataSource' node.");
        }
        String type = node.attributeValue("type");
        if (configMap.containsKey(type))
        {
            clazz = configMap.get(type);
        }
        if (clazz == null)
        {
            throw new ConfigException("DataSource type does not exists");
        }
        try
        {
            dsConfig = (DataSourceConfiguration)ReflectHelper.newFastInstance(clazz);
            dsConfig.setType(type);
            String id = node.attributeValue("id");
            if (id == null)
            {
                id = type;
            }
            String main = node.attributeValue("main");
            boolean isMain = false;
            if (main != null)
            {
                isMain = Boolean.parseBoolean(main);
            }
            String chainref = node.attributeValue("chainref");
            if (chainref != null)
            {
                dsConfig.setNextId(chainref);
            }
            List<?> nodes = node.selectNodes("property[@name][@value]");
            Iterator<?> it = nodes.iterator();
            Element el = null;
            Field field = null;
            while (it.hasNext())
            {
                el = (Element)it.next();
                String name = el.attributeValue("name");
                String value = el.attributeValue("value");
                field = ReflectHelper.getField(clazz, name);
                ReflectHelper.setFieldString(field, dsConfig, value);
            }
            el = (Element)node.selectSingleNode("resultSet");
            if (el != null)
            {
                ResultSetConfig rsconfig = dsConfig.getResultSetConfig();
                String rsType = el.attributeValue("type");
                String rsSensitive = el.attributeValue("sensitive");
                String rsConcurrency = el.attributeValue("concurrency");
                rsconfig.setType(rsType);
                rsconfig.setConcurrency(rsConcurrency);
                rsconfig.setSensitive(rsSensitive);
            }
            cfg.addDataSourceConfig(id, dsConfig, isMain);
        }
        catch (NoSuchFieldException e)
        {
            throw new ConfigException(e.getMessage(), e);
        }
        catch (ClassReflectException e)
        {
            throw new ConfigException(e.getMessage(), e);
        }
    }
    
    /**
     * Parse entity config
     * 
     * @param doc Document
     * @param cfg Configuration config
     * @throws ConfigException
     */
    private static void parseEntityXpath(Document doc, Configuration cfg)
        throws ConfigException
    {
        EntityConfiguration entityConfig = cfg.getEntityConfig();
        String xpath = "/darks/entities";
        Element node = (Element)doc.selectSingleNode(xpath);
        if (node == null)
            return;
        List<?> nodes = node.selectNodes("entity[@class]");
        Iterator<?> it = nodes.iterator();
        while (it.hasNext())
        {
            Element el = (Element)it.next();
            String alias = el.attributeValue("alias");
            String className = el.attributeValue("class");
            if (className == null || "".equals(className))
                continue;
            entityConfig.addEntityConfig(alias, className);
        }
        
        nodes = node.selectNodes("package[@name]");
        it = nodes.iterator();
        while (it.hasNext())
        {
            Element el = (Element)it.next();
            String pkgName = el.attributeValue("name");
            if (pkgName == null || "".equals(pkgName))
                continue;
            List<Class<?>> classList = ReflectHelper.scanPackageClasses(pkgName, true);
            for (Class<?> clazz : classList)
            {
                entityConfig.addEntityConfig(clazz.getSimpleName(), clazz.getName());
            }
        }
    }
    
    /**
     * Parse SqlMap config
     * 
     * @param doc Document
     * @param cfg Configuration config
     * @throws ConfigException
     */
    private static void parseSqlMapXpath(Document doc, Configuration cfg)
        throws ConfigException
    {
        List<String> sqlMapsPath = cfg.getSqlMapsPath();
        String xpath = "/darks/sqlMapGroup";
        Element node = (Element)doc.selectSingleNode(xpath);
        if (node == null)
            return;
        List<?> nodes = node.selectNodes("sqlMap");
        Iterator<?> it = nodes.iterator();
        while (it.hasNext())
        {
            Element el = (Element)it.next();
            String path = el.getTextTrim();
            if (path.indexOf("*") >= 0)
            {
                List<String> list = FileHelper.getRegexResourceFiles(path);
                for (String fpath : list)
                {
                    sqlMapsPath.add(fpath);
                }
            }
            else
            {
                if (!"".equals(path))
                {
                    sqlMapsPath.add(path);
                }
            }
        }
    }
    
    /**
     * Parse cache config
     * 
     * @param doc Document
     * @param cfg Configuration config
     * @throws ConfigException
     */
    private static void parseCacheXpath(Document doc, Configuration cfg)
        throws ConfigException
    {
        CacheConfiguration cacheConfig = cfg.getCacheConfig();
        String xpath = "/darks/cacheGroup";
        Element node = (Element)doc.selectSingleNode(xpath);
        if (node == null)
            return;
        String use = node.attributeValue("use");
        String type = node.attributeValue("type");
        String cacheId = node.attributeValue("cacheId");
        String synchronous = node.attributeValue("synchronous");
        // Whether to use cache
        if (use != null)
        {
            if ("true".equalsIgnoreCase(use))
                cacheConfig.setUseCache(true);
            else if ("false".equalsIgnoreCase(use))
                cacheConfig.setUseCache(false);
        }
        // Cache type
        if (type != null)
        {
            if ("auto".equals(type))
                cacheConfig.setCacheConfigType(CacheConfigType.Auto);
            else if ("manual".equals(type))
                cacheConfig.setCacheConfigType(CacheConfigType.Manual);
        }
        // Parse cache auto-id
        if (cacheId != null)
        {
            cacheConfig.setAutoCacheId(cacheId);
        }
        // Whether synchronous cache
        if (synchronous != null)
        {
            if ("true".equalsIgnoreCase(synchronous))
                cacheConfig.setSynchronous(true);
            else if ("false".equalsIgnoreCase(synchronous))
                cacheConfig.setSynchronous(false);
        }
        cacheConfig.readCacheConfig(node);
    }
}