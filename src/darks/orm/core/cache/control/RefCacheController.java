package darks.orm.core.cache.control;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Queue;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheList;
import darks.orm.core.cache.CacheObjectUpdater;
import darks.orm.core.cache.CacheController;
import darks.orm.core.data.xml.CacheConfigData;

@SuppressWarnings("unchecked")
public class RefCacheController implements CacheController
{
    
    private CacheConfigData cacheConfigData;
    
    private Queue<CacheKey> keysList;
    
    private Map<CacheKey, Object> entityMap;
    
    @SuppressWarnings("unused")
    private Map<CacheKey, CacheList> listMap;
    
    public RefCacheController()
    {
        
    }
    
    public RefCacheController(CacheConfigData cacheConfigData, Queue<CacheKey> keysList,
        Map<CacheKey, Object> entityMap, Map<CacheKey, CacheList> listMap)
    {
        this.cacheConfigData = cacheConfigData;
        this.keysList = keysList;
        this.entityMap = entityMap;
        this.listMap = listMap;
    }
    
    @Override
    public void cacheObject(CacheKey key, Object value)
    {
        if (!cacheConfigData.isEntirety())
        {
            Object oldval = getObject(key);
            if (oldval != null)
            {
                CacheObjectUpdater.update(key, oldval, value);
                return;
            }
        }
        else
        {
            if (entityMap.containsKey(key))
                return;
        }
        CacheControlStrategy strategy = cacheConfigData.getCacheStrategy();
        Reference<Object> ref = null;
        if (strategy == CacheControlStrategy.SoftRef)
        {
            ref = new SoftReference<Object>(value);
        }
        else if (strategy == CacheControlStrategy.WeakRef)
        {
            ref = new WeakReference<Object>(value);
        }
        
        entityMap.put(key, ref);
        
        keysList.offer(key);
    }
    
    @Override
    public Object getObject(CacheKey key)
    {
        if (!entityMap.containsKey(key))
            return null;
        Object value = null;
        Object ref = entityMap.get(key);
        if (ref instanceof SoftReference)
        {
            value = ((SoftReference)ref).get();
        }
        else if (ref instanceof WeakReference)
        {
            value = ((WeakReference)ref).get();
        }
        return value;
    }
    
}
