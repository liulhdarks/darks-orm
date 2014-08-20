package darks.orm.core.cache.thread;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Queue;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheList;
import darks.orm.core.cache.CacheObject;
import darks.orm.core.data.xml.CacheConfigData;
import darks.orm.core.session.SessionContext;
import darks.orm.util.LogHelper;

@SuppressWarnings("unchecked")
public class CacheCheckThread implements Runnable
{
    
    private Queue<CacheKey> keysList;
    
    private ConcurrentMap<CacheKey, Object> entityMap;
    
    private ConcurrentMap<CacheKey, CacheList> listMap;
    
    private CacheConfigData appConfigData;
    
    public CacheCheckThread()
    {
        
    }
    
    public CacheCheckThread(Queue<CacheKey> keysList, ConcurrentMap<CacheKey, Object> entityMap,
        ConcurrentMap<CacheKey, CacheList> listMap, CacheConfigData appConfigData)
    {
        this.keysList = keysList;
        this.entityMap = entityMap;
        this.listMap = listMap;
        this.appConfigData = appConfigData;
    }
    
    @Override
    public void run()
    {
        try
        {
            LogHelper.println("The cache object check thread has been started.");
            while (SessionContext.isInited())
            {
                int num = 0;
                for (Entry<CacheKey, Object> entry : entityMap.entrySet())
                {
                    CacheKey key = entry.getKey();
                    Object obj = entry.getValue();
                    if (obj == null)
                    {
                        removeKey(key);
                        continue;
                    }
                    Object value = obj;
                    if (obj instanceof WeakReference)
                    {
                        value = ((WeakReference)obj).get();
                    }
                    else if (obj instanceof SoftReference)
                    {
                        value = ((SoftReference)obj).get();
                    }
                    if (value == null)
                    {
                        removeKey(key);
                        continue;
                    }
                    
                    CacheObject cacheObject = (CacheObject)value;
                    long curTime = System.currentTimeMillis();
                    if (appConfigData.getIdleTime() > 0)
                    {
                        long tmp = curTime - cacheObject.getLastIdleTime();
                        if (tmp > appConfigData.getIdleTime())
                        {
                            removeKey(key);
                            continue;
                        }
                    }
                    if (appConfigData.getLiveTime() > 0)
                    {
                        long tmp = curTime - cacheObject.getLastLiveTime();
                        if (tmp > appConfigData.getLiveTime())
                        {
                            removeKey(key);
                            continue;
                        }
                    }
                    num++;
                    if (num > 512)
                    {
                        num = 0;
                        Thread.sleep(500);
                    }
                }
                Thread.sleep(1000);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * ÒÆ³ý»º´æ¶ÔÏó
     * 
     * @param key
     */
    private void removeKey(CacheKey key)
    {
        if (keysList.size() > 0)
            keysList.remove(key);
        if (entityMap.size() > 0)
            entityMap.remove(key);
        for (Entry<CacheKey, CacheList> entry : listMap.entrySet())
        {
            CacheKey tmpkey = entry.getKey();
            CacheList list = entry.getValue();
            Map<CacheKey, Object> map = list.getMap();
            if (map.containsKey(key))
            {
                listMap.remove(tmpkey);
            }
        }
    }
}
