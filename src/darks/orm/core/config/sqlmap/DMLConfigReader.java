package darks.orm.core.config.sqlmap;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import darks.orm.app.QueryEnumType;
import darks.orm.core.aspect.jython.PythonBuilder;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.DMLData;
import darks.orm.core.data.xml.DMLQueryData;
import darks.orm.core.data.xml.DMLUpdateData;
import darks.orm.core.data.xml.AspectData.AspectType;
import darks.orm.core.data.xml.DMLData.DMLType;
import darks.orm.core.data.xml.DMLQueryData.DMLQueryDataType;
import darks.orm.core.factory.TransformFactory;
import darks.orm.exceptions.ConfigException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.LogHelper;
import org.dom4j.Attribute;
import org.dom4j.Element;

@SuppressWarnings("unchecked")
public class DMLConfigReader
{
    
    private static final Logger logger = LoggerFactory.getLogger(DMLConfigReader.class);
    
    private SqlMapConfiguration sqlMapConfig;
    
    public DMLConfigReader(SqlMapConfiguration sqlMapConfig)
    {
        this.sqlMapConfig = sqlMapConfig;
    }
    
    /**
     * 读取DDL配置文件
     * 
     * @param element 元素
     */
    public void reader(Element element, File file)
    {
        String namesp = element.attributeValue("namespace");
        if (namesp == null)
            namesp = "";
        for (Iterator<Element> it = element.elementIterator(); it.hasNext();)
        {
            Element el = it.next();
            String name = el.getName().trim();
            if ("Query".equalsIgnoreCase(name))
            {
                readQuery(el, file, namesp);
            }
            else if ("Update".equalsIgnoreCase(name))
            {
                readUpdate(el, file, namesp);
            }
        }
    }
    
    /**
     * 读取<query>标签
     * 
     * @param element 元素
     */
    private DMLData readQuery(Element element, File file, String namesp)
    {
        DMLData dmlData = new DMLData();
        dmlData.setType(DMLType.Query);
        DMLQueryData queryData = new DMLQueryData();
        queryData.setQueryType(QueryEnumType.Auto);
        queryData.setAutoCascade(true);
        for (Iterator<Attribute> it = element.attributeIterator(); it.hasNext();)
        {
            try
            {
                Attribute at = it.next();
                String name = at.getName().trim();
                String value = at.getValue().trim();
                if ("id".equalsIgnoreCase(name))
                {
                    dmlData.setId(value);
                }
                else if ("resultType".equalsIgnoreCase(name))
                {
                    queryData.setResultType(value);
                    Class<?> cls = TransformFactory.getInstance().stringToEntityClass(value);
                    if (cls == null)
                    {
                        LogHelper.except(logger, value + " does not exists", ConfigException.class);
                    }
                    queryData.setResultClass(cls);
                }
                else if ("autoCascade".equalsIgnoreCase(name))
                {
                    if ("true".equalsIgnoreCase(value))
                        queryData.setAutoCascade(true);
                    else if ("false".equalsIgnoreCase(value))
                        queryData.setAutoCascade(false);
                }
                else if ("alias".equalsIgnoreCase(name))
                {
                    queryData.setAlias(value);
                }
                else if ("attribute".equalsIgnoreCase(name))
                {
                    queryData.setAttribute(value);
                }
                else if ("queryType".equalsIgnoreCase(name))
                {
                    if ("auto".equalsIgnoreCase(value))
                        queryData.setQueryType(QueryEnumType.Auto);
                    else if ("object".equalsIgnoreCase(value))
                        queryData.setQueryType(QueryEnumType.Object);
                    else if ("list".equalsIgnoreCase(value))
                        queryData.setQueryType(QueryEnumType.List);
                    else if ("page".equalsIgnoreCase(value))
                        queryData.setQueryType(QueryEnumType.Page);
                }
                else if ("cache".equalsIgnoreCase(name))
                {
                    if ("auto".equalsIgnoreCase(value))
                        dmlData.setAutoCache(true);
                    else if ("manual".equalsIgnoreCase(value))
                        dmlData.setAutoCache(false);
                }
                else if ("cacheId".equalsIgnoreCase(name))
                {
                    dmlData.setCacheId(value);
                }
            }
            catch (ConfigException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        if (dmlData.getId() == null)
            return null;
        
        String sql = element.getTextTrim();
        if (sql != null && !"".equals(sql))
        {
            // 读取普通查询
            queryData.setQueryDataType(DMLQueryDataType.Simple);
            queryData.setSQL(sql);
        }
        else
        {
            // 读取组合式查询
            if (!readConstitute(element, queryData))
            {
                // 读取选择查询
                if (!readSelect(element, queryData, ""))
                {
                    return null;
                }
            }
        }
        AspectData espectData = parseEspectXml(element);
        queryData.setAspectData(espectData);
        dmlData.setQueryData(queryData);
        if (!"".equals(namesp))
        {
            if (!namesp.endsWith("."))
                namesp += ".";
        }
        sqlMapConfig.addDMLData(namesp + dmlData.getId(), dmlData);
        return dmlData;
    }
    
    /**
     * 读取组合式查询
     * 
     * @param element 元素
     * @param queryData 查询数据
     * @return 是否有效
     */
    private boolean readConstitute(Element element, DMLQueryData queryData)
    {
        queryData.setQueryDataType(DMLQueryDataType.Constitute);
        Element queryElement = element.element("query");
        Element cstElement = element.element("constitute");
        if (queryElement == null || cstElement == null)
            return false;
        String sql = queryElement.getTextTrim();
        if (sql == null || "".equals(sql))
            return false;
        queryData.setSQL(sql);
        List<Element> items = cstElement.elements("item");
        for (Element item : items)
        {
            String value = item.attributeValue("value");
            if (value == null || "".equals(value))
                continue;
            String exsql = item.getTextTrim();
            if (exsql == null)
                exsql = "";
            // queryData.addSQL(value, sql +" "+ exsql);
            queryData.addSQL(value, exsql);
        }
        return true;
    }
    
    /**
     * 读取选择查询
     * 
     * @param element 元素
     * @param queryData 查询数据
     * @param pId 父级值
     * @return 是否有效
     */
    private boolean readSelect(Element element, DMLQueryData queryData, String pVal)
    {
        queryData.setQueryDataType(DMLQueryDataType.Select);
        List<Element> selects = element.elements("select");
        if (selects == null || selects.size() == 0)
            return false;
        for (Element sel : selects)
        {
            String value = sel.attributeValue("value");
            if (value == null || "".equals(value))
                continue;
            
            List<Element> chsel = sel.elements("select");
            if (chsel.size() > 0)
            {
                readSelect(sel, queryData, pVal + value + "@");
            }
            else
            {
                String sql = sel.getTextTrim();
                String key = pVal + value;
                if (key.endsWith("@"))
                {
                    key = key.substring(0, key.length() - 1);
                }
                queryData.addSQL(key, sql);
            }
        }
        return true;
    }
    
    /**
     * 读取<update>标签
     * 
     * @param element 元素
     */
    private DMLData readUpdate(Element element, File file, String namesp)
    {
        DMLData dmlData = new DMLData();
        String sql = element.getTextTrim();
        dmlData.setType(DMLType.Update);
        DMLUpdateData updateData = new DMLUpdateData();
        updateData.setSql(sql);
        for (Iterator<Attribute> it = element.attributeIterator(); it.hasNext();)
        {
            try
            {
                Attribute at = it.next();
                String name = at.getName().trim();
                String value = at.getValue().trim();
                if ("id".equalsIgnoreCase(name))
                {
                    dmlData.setId(value);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if (dmlData.getId() == null)
            return null;
        AspectData aspectData = parseEspectXml(element);
        updateData.setAspectData(aspectData);
        dmlData.setUpdateData(updateData);
        if (!"".equals(namesp))
        {
            if (!namesp.endsWith("."))
                namesp += ".";
        }
        sqlMapConfig.addDMLData(namesp + dmlData.getId(), dmlData);
        return dmlData;
    }
    
    /**
     * 解析AOP XML
     * 
     * @param element XML元素
     * @return
     */
    private AspectData parseEspectXml(Element element)
    {
        Element aspectEl = element.element("aspect");
        if (aspectEl == null)
            return null;
        AspectData espectData = new AspectData();
        boolean isNext = false;
        Element jythonEl = aspectEl.element("jython");
        if (jythonEl != null)
        {
            espectData.setAspectType(AspectType.JYTHON);
            Attribute attr = jythonEl.attribute("className");
            if (attr == null)
                return null;
            espectData.setClassName(attr.getValue().trim());
            String text = jythonEl.getText();
            if (text == null || "".equals(text.trim()))
            {
                String before = jythonEl.elementText("before");
                String after = jythonEl.elementText("after");
                if (before == null || "".equals(before.trim()))
                {
                    if (after == null || "".equals(after.trim()))
                        return null;
                }
                text = PythonBuilder.buildBody(espectData.getClassName(), before, after);
            }
            espectData.setContent(text);
            isNext = true;
        }
        
        Element pyfileEl = aspectEl.element("pyfile");
        if (pyfileEl != null && isNext == false)
        {
            espectData.setAspectType(AspectType.PYFILE);
            Attribute attr = pyfileEl.attribute("className");
            if (attr == null)
                return null;
            espectData.setClassName(attr.getValue().trim());
            espectData.setContent(pyfileEl.getTextTrim());
            isNext = true;
        }
        
        Element javaClassEl = aspectEl.element("javaClass");
        if (javaClassEl != null && isNext == false)
        {
            espectData.setAspectType(AspectType.JAVACLASS);
            espectData.setClassName(javaClassEl.getTextTrim());
            isNext = true;
        }
        
        Element jsEl = aspectEl.element("javascript");
        if (jsEl != null && isNext == false)
        {
            espectData.setAspectType(AspectType.JAVASCRIPT);
            espectData.setContent(jsEl.getTextTrim());
            isNext = true;
        }
        
        Element jsfileEl = aspectEl.element("jsfile");
        if (jsfileEl != null && isNext == false)
        {
            espectData.setAspectType(AspectType.JSFILE);
            espectData.setContent(jsfileEl.getTextTrim());
            isNext = true;
        }
        
        if (isNext)
        {
            return espectData;
        }
        return null;
    }
    
    public SqlMapConfiguration getSqlMapConfig()
    {
        return sqlMapConfig;
    }
    
    public void setSqlMapConfig(SqlMapConfiguration sqlMapConfig)
    {
        this.sqlMapConfig = sqlMapConfig;
    }
    
}
