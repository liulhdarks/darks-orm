package darks.orm.core.cache;

public interface CacheController
{
    
    public void cacheObject(CacheKey key, Object value)
        throws Exception;
    
    public Object getObject(CacheKey key)
        throws Exception;
}
