package darks.orm.core.cache.scope;

import darks.orm.core.cache.CacheKey;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class EhCacheFactory implements CacheFactory
{
    
    private EhCacheProvider provider;
    
    private Cache cache;
    
    @SuppressWarnings("unused")
    private EhCacheFactory()
    {
        
    }
    
    public EhCacheFactory(EhCacheProvider provider, Cache cache)
    {
        this.provider = provider;
        this.cache = cache;
    }
    
    @Override
    public void cacheObject(CacheKey key, Object obj)
        throws Exception
    {
        if (key == null || obj == null)
            return;
        EhCacheElement el = new EhCacheElement(key, key.getCount(), obj);
        cache.put(new Element(key, el));
    }
    
    @Override
    public boolean containKey(CacheKey key)
    {
        try
        {
            if (getObject(key) != null)
            {
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void debug()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void flush()
    {
        cache.flush();
    }
    
    @Override
    public Object getObject(CacheKey key)
        throws Exception
    {
        Element el = cache.get(key);
        if (el == null)
            return null;
        EhCacheElement cel = (EhCacheElement)el.getObjectValue();
        key.setCount(cel.getCount());
        return cel.getValue();
    }
    
    public EhCacheProvider getProvider()
    {
        return provider;
    }
    
    public Cache getCache()
    {
        return cache;
    }
    
}
