package darks.orm.core.data.xml;

public class DMLData
{
    
    public enum DMLType
    {
        Query, Update
    };
    
    private String id;
    
    private DMLType type;
    
    private boolean autoCache = true;
    
    private String cacheId;
    
    private DMLQueryData queryData;
    
    private DMLUpdateData updateData;
    
    public DMLData()
    {
        
    }
    
    public DMLData(String id, DMLType type)
    {
        super();
        this.id = id;
        this.type = type;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public DMLType getType()
    {
        return type;
    }
    
    public void setType(DMLType type)
    {
        this.type = type;
    }
    
    public DMLQueryData getQueryData()
    {
        return queryData;
    }
    
    public void setQueryData(DMLQueryData queryData)
    {
        this.queryData = queryData;
    }
    
    public DMLUpdateData getUpdateData()
    {
        return updateData;
    }
    
    public void setUpdateData(DMLUpdateData updateData)
    {
        this.updateData = updateData;
    }
    
    public boolean isAutoCache()
    {
        return autoCache;
    }
    
    public void setAutoCache(boolean autoCache)
    {
        this.autoCache = autoCache;
    }
    
    public String getCacheId()
    {
        return cacheId;
    }
    
    public void setCacheId(String cacheId)
    {
        this.cacheId = cacheId;
    }
    
}
