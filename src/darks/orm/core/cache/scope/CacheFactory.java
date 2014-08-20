package darks.orm.core.cache.scope;

import darks.orm.core.cache.CacheKey;

public interface CacheFactory
{
    
    /**
     * 缓存对象
     * 
     * @param key 缓存KEY
     * @param obj 对象实例
     * @throws Exception
     */
    public void cacheObject(CacheKey key, Object obj)
        throws Exception;
    
    /**
     * 获得对象
     * 
     * @param key 缓存KEY
     * @return 对象实例
     * @throws Exception
     */
    public Object getObject(CacheKey key)
        throws Exception;
    
    /**
     * 缓存中是否存在拥有此缓存KEY的对象
     * 
     * @param key 缓存KEY
     * @return true存在 false不存在
     */
    public boolean containKey(CacheKey key);
    
    public void flush();
    
    public void debug();
}
