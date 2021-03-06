/**
 * 
 * Copyright 2014 The Darks ORM Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package darks.orm.core.cache.thread;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheList;
import darks.orm.core.cache.CacheObject;
import darks.orm.core.data.xml.CacheConfigData;
import darks.orm.core.session.SessionContext;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

@SuppressWarnings("unchecked")
public class CacheCheckThread implements Runnable
{
    private static final Logger logger = LoggerFactory.getLogger(CacheCheckThread.class);
    
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
            logger.debug("The cache object check thread has been started.");
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
                        value = ((WeakReference<Object>)obj).get();
                    }
                    else if (obj instanceof SoftReference)
                    {
                        value = ((SoftReference<Object>)obj).get();
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
     * �Ƴ��������
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