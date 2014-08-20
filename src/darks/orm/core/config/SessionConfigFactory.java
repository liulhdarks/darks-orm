/**
 * 类名:SessionConfigFactory.java
 * 作者:刘力华
 * 创建时间:2012-5-27
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.core.config;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import darks.orm.core.config.CacheConfiguration.CacheConfigType;
import darks.orm.exceptions.ClassReflectException;
import darks.orm.exceptions.ConfigException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.FileHelper;
import darks.orm.util.LogHelper;
import darks.orm.util.ReflectHelper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public final class SessionConfigFactory
{
    
    private static final Logger logger = LoggerFactory.getLogger(SessionConfigFactory.class);
    
    private static final String DEFAULT_CONFIG_PATH = "/darks.xml";
    
    private static final String DEFAULT_CONFIG_NAMESPACE = "http://www.darks.org/schema/darks";
    
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
     * 获取配置文件实例
     * 
     * @return 配置文件实例
     * @throws ConfigException
     */
    public static Configuration getConfiguration()
        throws ConfigException
    {
        URL url = SessionConfigFactory.class.getResource(DEFAULT_CONFIG_PATH);
        if (url == null)
        {
            LogHelper.except(logger,
                "'" + DEFAULT_CONFIG_PATH + "' configuration file does not exists.",
                ConfigException.class);
        }
        else
        {
            LogHelper.println("Load '" + DEFAULT_CONFIG_PATH + "' configure file ['" + url.toString() + "']");
        }
        String path = url.getFile();
        path = path.replace("%20", " ");
        return getConfiguration(path);
    }
    
    /**
     * 获取配置文件实例
     * 
     * @param configPath 配置文件路径
     * @return 配置文件实例
     * @throws ConfigException
     */
    public static Configuration getConfiguration(String configPath)
        throws ConfigException
    {
        Configuration cfg = new Configuration();
        try
        {
            File f = new File(configPath);
            SAXReader reader = new SAXReader();
            Map<String, String> map = new HashMap<String, String>();
            map.put("d", DEFAULT_CONFIG_NAMESPACE);
            reader.getDocumentFactory().setXPathNamespaceURIs(map);
            Document doc = reader.read(f);
            // 解析DataSource
            parseDataSourceXpath(doc, cfg);
            // 解析日志配置
            parseLoggerXpath(doc, cfg);
            // 解析实体配置
            parseEntityXpath(doc, cfg);
            // 解析SqlMap路径配置
            parseSqlMapXpath(doc, cfg);
            // 解析缓存配置
            parseCacheXpath(doc, cfg);
        }
        catch (DocumentException e)
        {
            LogHelper.except(logger,
                "fail to read document'" + configPath + "' configuration file.",
                ConfigException.class);
        }
        return cfg;
    }
    
    /**
     * 解析DataSource配置
     * 
     * @param doc Document
     * @param cfg Configuration配置
     * @throws ConfigException
     */
    private static void parseDataSourceXpath(Document doc, Configuration cfg)
        throws ConfigException
    {
        String xpath = "/d:darks/d:dataSource[@type]";
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
     * 解析DataSource单个元素
     * 
     * @param node XML元素节点
     * @param cfg Configuration配置
     * @throws ConfigException
     */
    private static void parseDataSourceNode(Element node, Configuration cfg)
        throws ConfigException
    {
        DataSourceConfiguration dsConfig = null;
        Class<?> clazz = null;
        if (node == null)
        {
            LogHelper.except(logger, "document does not has 'dataSource' node.", ConfigException.class);
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
            List<?> nodes = node.selectNodes("d:property[@name][@value]");
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
            el = (Element)node.selectSingleNode("d:resultSet");
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
            LogHelper.except(logger, e.toString(), ConfigException.class);
        }
        catch (ClassReflectException e)
        {
            LogHelper.except(logger, e.toString(), ConfigException.class);
        }
    }
    
    /**
     * 解析Logger配置
     * 
     * @param doc Document
     * @param cfg Configuration配置
     * @throws ConfigException
     */
    private static void parseLoggerXpath(Document doc, Configuration cfg)
        throws ConfigException
    {
        String xpath = "/d:darks/d:logger";
        Element node = (Element)doc.selectSingleNode(xpath);
        if (node == null)
            return;
        Element showSqlNode = (Element)node.selectSingleNode("d:showSql");
        Element showLogNode = (Element)node.selectSingleNode("d:showLog");
        Element writeLogNode = (Element)node.selectSingleNode("d:writeLog");
        LoggerConfiguration config = cfg.Logger();
        if (showSqlNode != null)
        {
            String val = showSqlNode.getTextTrim();
            config.setShowSql(Boolean.parseBoolean(val));
        }
        if (showLogNode != null)
        {
            String val = showLogNode.getTextTrim();
            config.setShowLog(Boolean.parseBoolean(val));
        }
        if (writeLogNode != null)
        {
            String val = writeLogNode.getTextTrim();
            config.setWriteLog(Boolean.parseBoolean(val));
        }
    }
    
    /**
     * 解析实体配置
     * 
     * @param doc Document
     * @param cfg Configuration配置
     * @throws ConfigException
     */
    private static void parseEntityXpath(Document doc, Configuration cfg)
        throws ConfigException
    {
        EntityConfiguration entityConfig = cfg.getEntityConfig();
        String xpath = "/d:darks/d:entitys";
        Element node = (Element)doc.selectSingleNode(xpath);
        if (node == null)
            return;
        List<?> nodes = node.selectNodes("d:entity[@class]");
        Iterator<?> it = nodes.iterator();
        while (it.hasNext())
        {
            Element el = (Element)it.next();
            String alias = el.attributeValue("alias");
            String className = el.attributeValue("class");
            if (className == null)
                continue;
            entityConfig.addEntityConfig(alias, className);
        }
    }
    
    /**
     * 解析SqlMap配置
     * 
     * @param doc Document
     * @param cfg Configuration配置
     * @throws ConfigException
     */
    private static void parseSqlMapXpath(Document doc, Configuration cfg)
        throws ConfigException
    {
        List<String> sqlMapsPath = cfg.getSqlMapsPath();
        String xpath = "/d:darks/d:sqlMapGroup";
        Element node = (Element)doc.selectSingleNode(xpath);
        if (node == null)
            return;
        List<?> nodes = node.selectNodes("d:sqlMap");
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
     * 解析缓存配置
     * 
     * @param doc Document
     * @param cfg Configuration配置
     * @throws ConfigException
     */
    private static void parseCacheXpath(Document doc, Configuration cfg)
        throws ConfigException
    {
        CacheConfiguration cacheConfig = cfg.getCacheConfig();
        String xpath = "/d:darks/d:cacheGroup";
        Element node = (Element)doc.selectSingleNode(xpath);
        if (node == null)
            return;
        String use = node.attributeValue("use");
        String type = node.attributeValue("type");
        String cacheId = node.attributeValue("cacheId");
        String synchronous = node.attributeValue("synchronous");
        // 是否使用缓存
        if (use != null)
        {
            if ("true".equalsIgnoreCase(use))
                cacheConfig.setUseCache(true);
            else if ("false".equalsIgnoreCase(use))
                cacheConfig.setUseCache(false);
        }
        // 使用类型 自动/手动
        if (type != null)
        {
            if ("auto".equals(type))
                cacheConfig.setCacheConfigType(CacheConfigType.Auto);
            else if ("manual".equals(type))
                cacheConfig.setCacheConfigType(CacheConfigType.Manual);
        }
        // 自动缓存编号
        if (cacheId != null)
        {
            cacheConfig.setAutoCacheId(cacheId);
        }
        // 是否异步缓存
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
