package darks.orm.core.cache.strategy;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheObject;

public class RefCopyStrategy implements CopyStrategy
{
    
    private static final long serialVersionUID = 2321965412872827152L;
    
    /**
     * 缓存读
     * 
     * @param cacheObject 缓存对象
     * @param key 键值
     * @param value 值
     * @return
     * @throws Exception
     */
    @Override
    public Object read(CacheObject cacheObject, CacheKey key, Object value)
        throws Exception
    {
        if (value == null)
            return null;
        return value;
    }
    
    /**
     * 缓存写
     * 
     * @param cacheObject 缓存对象
     * @param key 键值
     * @param value 值
     * @return
     * @throws Exception
     */
    @Override
    public Object write(CacheObject cacheObject, CacheKey key, Object value)
        throws Exception
    {
        return value;
    }
    
}
