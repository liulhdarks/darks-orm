package darks.orm.core.cache.control;

import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheList;
import darks.orm.core.cache.CacheObjectUpdater;
import darks.orm.core.cache.CacheController;
import darks.orm.core.data.xml.CacheConfigData;

public class FifoCacheController implements CacheController
{
    
    private CacheConfigData cacheConfigData;
    
    private Queue<CacheKey> keysList;
    
    private Map<CacheKey, Object> entityMap;
    
    private Map<CacheKey, CacheList> listMap;
    
    public FifoCacheController()
    {
        
    }
    
    public FifoCacheController(CacheConfigData cacheConfigData, Queue<CacheKey> keysList,
        Map<CacheKey, Object> entityMap, Map<CacheKey, CacheList> listMap)
    {
        this.cacheConfigData = cacheConfigData;
        this.keysList = keysList;
        this.entityMap = entityMap;
        this.listMap = listMap;
    }
    
    @Override
    public void cacheObject(CacheKey key, Object value)
        throws Exception
    {
        // if(entityMap.containsKey(key))return;
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
        // System.out.println("fifo cache in");
        // 保存到Map中
        entityMap.put(key, value);
        // 保存key到keyList
        keysList.offer(key);
        // 如果当前key的数量大于缓存容量时，移除keyList和cache中的第一个元素，达到先进先出的目的
        if (keysList.size() > cacheConfigData.getMaxObject())
        {
            try
            {
                Object oldestKey = keysList.poll();
                if (entityMap.size() > 0)
                    entityMap.remove(oldestKey);
                for (Entry<CacheKey, CacheList> entry : listMap.entrySet())
                {
                    CacheKey tmpkey = entry.getKey();
                    CacheList list = entry.getValue();
                    Map<CacheKey, Object> map = list.getMap();
                    if (map.containsKey(oldestKey))
                    {
                        listMap.remove(tmpkey);
                    }
                }
            }
            catch (IndexOutOfBoundsException e)
            {
                // ignore
            }
        }
        
    }
    
    @Override
    public Object getObject(CacheKey key)
        throws Exception
    {
        if (!entityMap.containsKey(key))
            return null;
        // System.out.println("fifo cache out");
        return entityMap.get(key);
    }
    
}
