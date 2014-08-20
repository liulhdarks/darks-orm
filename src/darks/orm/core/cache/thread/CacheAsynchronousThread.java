package darks.orm.core.cache.thread;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.scope.CacheFactory;

/**
 * 类名:CacheAsynchronousThread 作者:刘力华 创建时间:2012.03.04 版本:1.0.0 alpha
 * 版权:CopyRight(c)2012 刘力华 该项目工程所有权归刘力华所有 描述:缓存异步压入线程
 */
public class CacheAsynchronousThread implements Runnable
{
    
    private CacheFactory factory;
    
    private CacheKey key;
    
    @SuppressWarnings("unused")
    private String cacheId;
    
    private Object value;
    
    public CacheAsynchronousThread()
    {
        
    }
    
    public CacheAsynchronousThread(CacheFactory factory, CacheKey key, String cacheId, Object value)
    {
        this.factory = factory;
        this.key = key;
        this.cacheId = cacheId;
        this.value = value;
    }
    
    @Override
    public void run()
    {
        if (factory == null)
            return;
        try
        {
            if (factory.containKey(key))
                return;
            factory.cacheObject(key, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
