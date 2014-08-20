package darks.orm.core.cache.strategy;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheObject;
import darks.orm.util.ByteHelper;

public class SerialCopyStrategy implements CopyStrategy
{
    
    private static final long serialVersionUID = 6419864241489980487L;
    
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
        byte[] bytes = (byte[])value;
        return ByteHelper.ByteToObject(bytes);
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
        byte[] bytes = ByteHelper.ObjectToByte(value);
        cacheObject.setType(0);
        return bytes;
    }
    
}
