package darks.orm.core.data.xml;

import java.util.HashMap;
import java.util.Map;

import darks.orm.app.QueryEnumType;

@SuppressWarnings("unchecked")
public class InterfaceMethodData
{
    
    private String namespace;
    
    private int pageIndex = -1;
    
    private int pageSizeIndex = -1;
    
    private int valuesIndex = -1;
    
    private String sql;
    
    private Class resultClass;
    
    private QueryEnumType queryEnumType;
    
    private String cacheId;
    
    private String attribute;
    
    private String alias;
    
    private boolean autoCache = true;
    
    private boolean autoCascade = true;
    
    private Map<String, Integer> argumentsMap;
    
    public InterfaceMethodData()
    {
        argumentsMap = new HashMap<String, Integer>();
    }
    
    public InterfaceMethodData(String namespace, int pageIndex, int pageSizeIndex, int valuesIndex, String sql,
        Class resultClass, String cacheId, String attribute, String alias, boolean autoCache, boolean autoCascade)
    {
        argumentsMap = new HashMap<String, Integer>();
        this.namespace = namespace;
        this.pageIndex = pageIndex;
        this.pageSizeIndex = pageSizeIndex;
        this.valuesIndex = valuesIndex;
        this.sql = sql;
        this.resultClass = resultClass;
        this.cacheId = cacheId;
        this.attribute = attribute;
        this.alias = alias;
        this.autoCache = autoCache;
        this.autoCascade = autoCascade;
    }
    
    public int getArgumentIndex(String argName)
    {
        if (argumentsMap.containsKey(argName))
        {
            return argumentsMap.get(argName);
        }
        return -1;
    }
    
    public void addArgument(int index, String argName)
    {
        argumentsMap.put(argName, index);
    }
    
    public Map<String, Integer> getArgumentsMap()
    {
        return argumentsMap;
    }
    
    public int getPageIndex()
    {
        return pageIndex;
    }
    
    public void setPageIndex(int pageIndex)
    {
        this.pageIndex = pageIndex;
    }
    
    public int getPageSizeIndex()
    {
        return pageSizeIndex;
    }
    
    public void setPageSizeIndex(int pageSizeIndex)
    {
        this.pageSizeIndex = pageSizeIndex;
    }
    
    public int getValuesIndex()
    {
        return valuesIndex;
    }
    
    public void setValuesIndex(int valuesIndex)
    {
        this.valuesIndex = valuesIndex;
    }
    
    public String getNamespace()
    {
        return namespace;
    }
    
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }
    
    public String getSql()
    {
        return sql;
    }
    
    public void setSql(String sql)
    {
        this.sql = sql;
    }
    
    public Class getResultClass()
    {
        return resultClass;
    }
    
    public void setResultClass(Class resultClass)
    {
        this.resultClass = resultClass;
    }
    
    public QueryEnumType getQueryEnumType()
    {
        return queryEnumType;
    }
    
    public void setQueryEnumType(QueryEnumType queryEnumType)
    {
        this.queryEnumType = queryEnumType;
    }
    
    public String getCacheId()
    {
        return cacheId;
    }
    
    public void setCacheId(String cacheId)
    {
        this.cacheId = cacheId;
    }
    
    public String getAttribute()
    {
        return attribute;
    }
    
    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }
    
    public String getAlias()
    {
        return alias;
    }
    
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
    
    public boolean isAutoCache()
    {
        return autoCache;
    }
    
    public void setAutoCache(boolean autoCache)
    {
        this.autoCache = autoCache;
    }
    
    public boolean isAutoCascade()
    {
        return autoCascade;
    }
    
    public void setAutoCascade(boolean autoCascade)
    {
        this.autoCascade = autoCascade;
    }
    
}
