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

package darks.orm.core.cache.control;

import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheList;
import darks.orm.core.cache.CacheObjectUpdater;
import darks.orm.core.cache.CacheController;
import darks.orm.core.data.xml.CacheConfigData;

public class LruCacheController implements CacheController
{
    
    private CacheConfigData cacheConfigData;
    
    private Queue<CacheKey> keysList;
    
    private Map<CacheKey, Object> entityMap;
    
    private Map<CacheKey, CacheList> listMap;
    
    private Lock lock = new ReentrantLock();
    
    public LruCacheController()
    {
        
    }
    
    public LruCacheController(CacheConfigData cacheConfigData, Queue<CacheKey> keysList,
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
        lock.lock();
        // System.out.println("lru cache in");
        try
        {
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
        finally
        {
            lock.unlock();
        }
    }
    
    @Override
    public Object getObject(CacheKey key)
    {
        if (!entityMap.containsKey(key))
            return null;
        // System.out.println("lru cache out");
        lock.lock();
        try
        {
            Object ret = entityMap.get(key);
            if (keysList.size() > 1)
            {
                keysList.remove(key);
                if (key != null && ret != null)
                {
                    keysList.offer(key);
                }
            }
            return ret;
        }
        finally
        {
            lock.unlock();
        }
    }
    
}