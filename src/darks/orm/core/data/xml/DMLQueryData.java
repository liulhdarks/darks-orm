package darks.orm.core.data.xml;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import darks.orm.app.QueryEnumType;

@SuppressWarnings("unchecked")
public class DMLQueryData implements Cloneable
{
    
    private ConcurrentMap<String, String> sqlMap = new ConcurrentHashMap<String, String>();
    
    private ConcurrentMap<Integer, String> sqlConstituteMap = new ConcurrentHashMap<Integer, String>();
    
    private static final String DEFAULT_KEY = "0000";
    
    private static final int INIT_BUFFER_SIZE = 256;
    
    private static final int INIT_HASH_VALUE = 2;
    
    private static final int HASH_VALUE_PARAM = 32;
    
    public enum DMLQueryDataType
    {
        Simple, Select, Constitute
    };
    
    private String resultType;
    
    private Class resultClass;
    
    private boolean autoCascade;
    
    private String alias;
    
    private String attribute;
    
    private QueryEnumType queryType;
    
    private DMLQueryDataType queryDataType;
    
    private AspectData aspectData;
    
    public DMLQueryData()
    {
        
    }
    
    public DMLQueryData(String resultType, boolean autoCascade, String alias, String attribute,
        QueryEnumType queryType, DMLQueryDataType queryDataType)
    {
        this.resultType = resultType;
        this.autoCascade = autoCascade;
        this.alias = alias;
        this.attribute = attribute;
        this.queryType = queryType;
        this.queryDataType = queryDataType;
    }
    
    public void setSQL(String sql)
    {
        sqlMap.put(DEFAULT_KEY, sql);
    }
    
    public void addSQL(String key, String sql)
    {
        sqlMap.put(key, sql);
    }
    
    public String getSql()
    {
        return getSql(DEFAULT_KEY);
    }
    
    public String getSql(String key)
    {
        if (key == null)
            return null;
        if (key.indexOf("@") < 0)
        {
            key = key.replace(",", "@");
        }
        if (sqlMap.containsKey(key))
        {
            return sqlMap.get(key);
        }
        return null;
    }
    
    public String getSql(Object[] values)
    {
        String key = null;
        if (queryDataType == DMLQueryDataType.Simple || values == null)
        {
            key = DEFAULT_KEY;
            return getSql(key);
        }
        else if (queryDataType == DMLQueryDataType.Constitute)
        {
            return getConstituteSql(values);
        }
        else if (queryDataType == DMLQueryDataType.Select)
        {
            return getSelectSql(values);
        }
        return getSql(key);
    }
    
    public String getConstituteSql(Object[] values)
    {
        int key = hash(values);
        String sql = sqlConstituteMap.get(key);
        if (sql != null)
        {
            return sql;
        }
        sql = getSql(DEFAULT_KEY);
        StringBuffer buf = new StringBuffer(INIT_BUFFER_SIZE);
        buf.append(sql);
        for (Object val : values)
        {
            if (val == null)
                continue;
            sql = getSql(String.valueOf(val));
            if (sql == null)
                continue;
            if (!sql.startsWith(" "))
                buf.append(" ");
            buf.append(sql);
        }
        sql = buf.toString();
        sqlConstituteMap.put(key, sql);
        return sql;
    }
    
    public String getSelectSql(Object[] values)
    {
        String key = null;
        StringBuffer buf = new StringBuffer();
        if (values != null)
        {
            for (Object val : values)
            {
                buf.append(val);
                buf.append("@");
            }
        }
        if (buf.length() > 0)
            buf.deleteCharAt(buf.length() - 1);
        key = buf.toString();
        return getSql(key);
    }
    
    private int hash(Object[] values)
    {
        int result = INIT_HASH_VALUE;
        return getObjectsHashCode(result, values);
    }
    
    private int getObjectHashCode(Object obj, int ret)
    {
        int result = HASH_VALUE_PARAM * ret;
        if (obj == null)
            return result;
        if (obj instanceof Boolean)
        {
            boolean bln = (Boolean)obj;
            result = result + (bln ? 0 : 1);
        }
        else if (obj instanceof Byte || obj instanceof Character || obj instanceof Short || obj instanceof Integer)
        {
            result = result + (Integer)obj;
        }
        else if (obj instanceof Long)
        {
            long along = (Long)obj;
            result = result + (int)(along ^ (along >>> 32));
            ;
        }
        else if (obj instanceof Float)
        {
            float afloat = (Float)obj;
            result = result + Float.floatToIntBits(afloat);
        }
        else if (obj instanceof Double)
        {
            double adouble = (Double)obj;
            long tolong = Double.doubleToLongBits(adouble);
            result = result + (int)(tolong ^ (tolong >>> 32));
        }
        else
        {
            result = result + obj.hashCode();
        }
        return result;
    }
    
    private int getObjectsHashCode(int result, Object[] objs)
    {
        if (objs == null)
            return HASH_VALUE_PARAM * result;
        for (int i = 0; i < objs.length; i++)
        {
            result = getObjectHashCode(objs[i], result);
        }
        return result;
    }
    
    public ConcurrentMap<String, String> getSqls()
    {
        return sqlMap;
    }
    
    public ConcurrentMap<String, String> getSqlMap()
    {
        return sqlMap;
    }
    
    public void setSqlMap(ConcurrentMap<String, String> sqlMap)
    {
        this.sqlMap = sqlMap;
    }
    
    public String getResultType()
    {
        return resultType;
    }
    
    public void setResultType(String resultType)
    {
        this.resultType = resultType;
    }
    
    public boolean isAutoCascade()
    {
        return autoCascade;
    }
    
    public void setAutoCascade(boolean autoCascade)
    {
        this.autoCascade = autoCascade;
    }
    
    public String getAlias()
    {
        return alias;
    }
    
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
    
    public String getAttribute()
    {
        return attribute;
    }
    
    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }
    
    public QueryEnumType getQueryType()
    {
        return queryType;
    }
    
    public void setQueryType(QueryEnumType queryType)
    {
        this.queryType = queryType;
    }
    
    public DMLQueryDataType getQueryDataType()
    {
        return queryDataType;
    }
    
    public void setQueryDataType(DMLQueryDataType queryDataType)
    {
        this.queryDataType = queryDataType;
    }
    
    public Class getResultClass()
    {
        return resultClass;
    }
    
    public void setResultClass(Class resultClass)
    {
        this.resultClass = resultClass;
    }
    
    public AspectData getAspectData()
    {
        return aspectData;
    }
    
    public void setAspectData(AspectData aspectData)
    {
        this.aspectData = aspectData;
    }
    
    public DMLQueryData clone()
    {
        try
        {
            return (DMLQueryData)super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
