package darks.orm.core.cache.control;

import java.util.Map;
import java.util.Queue;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheList;
import darks.orm.core.cache.CacheController;
import darks.orm.core.data.xml.CacheConfigData;

public class CacheControllerFactroy
{
    
    public static CacheController getCacheController(CacheConfigData cacheConfigData, Queue<CacheKey> keysList,
        Map<CacheKey, Object> entityMap, Map<CacheKey, CacheList> listMap)
    {
        CacheControlStrategy strategy = cacheConfigData.getCacheStrategy();
        if (strategy == CacheControlStrategy.Fifo)
        {
            return new FifoCacheController(cacheConfigData, keysList, entityMap, listMap);
        }
        else if (strategy == CacheControlStrategy.SoftRef || strategy == CacheControlStrategy.WeakRef)
        {
            return new RefCacheController(cacheConfigData, keysList, entityMap, listMap);
        }
        else
        {
            return new LruCacheController(cacheConfigData, keysList, entityMap, listMap);
        }
    }
}
