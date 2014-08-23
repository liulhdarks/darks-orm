package darks.orm.core.cache.thread;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.scope.CacheFactory;


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
