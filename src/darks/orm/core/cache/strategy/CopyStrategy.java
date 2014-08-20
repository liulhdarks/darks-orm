package darks.orm.core.cache.strategy;

import java.io.Serializable;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheObject;

public interface CopyStrategy extends Serializable
{
    
    /**
     * 缓存写
     * 
     * @param cacheObject 缓存对象
     * @param key 键值
     * @param value 值
     * @return
     * @throws Exception
     */
    public Object write(CacheObject cacheObject, CacheKey key, Object value)
        throws Exception;
    
    /**
     * 缓存读
     * 
     * @param cacheObject 缓存对象
     * @param key 键值
     * @param value 值
     * @return
     * @throws Exception
     */
    public Object read(CacheObject cacheObject, CacheKey key, Object value)
        throws Exception;
    
}
