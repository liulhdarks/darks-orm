package darks.orm.core.cache.strategy;

import java.util.List;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheObject;
import darks.orm.util.ByteHelper;

@SuppressWarnings("unchecked")
public class FieldCopyStrategy implements CopyStrategy
{
    
    private static final long serialVersionUID = 2985865563487870555L;
    
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
        if (cacheObject.getType() == 0)
        {
            byte[] bytes = (byte[])value;
            return ByteHelper.ByteToObject(null, key.getData().getClassOrignal(), bytes);
        }
        else
        {
            byte[][] bytes = (byte[][])value;
            return ByteHelper.ByteToList(null, key.getData().getClassOrignal(), bytes);
        }
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
        if (value instanceof List)
        {
            cacheObject.setType(1);
            return ByteHelper.ListToByte(key.getData().getClassOrignal(), (List)value);
        }
        else
        {
            cacheObject.setType(0);
            return ByteHelper.ObjectToByte(key.getData().getClassOrignal(), value);
        }
    }
    
}
