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

package darks.orm.core.config;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import darks.orm.core.cache.control.CacheControlStrategy;
import darks.orm.core.cache.scope.CacheScopeType;
import darks.orm.core.data.xml.CacheConfigData;
import org.dom4j.Attribute;
import org.dom4j.Element;


@SuppressWarnings("unchecked")
public class CacheConfiguration
{
    
    public enum CopyStrategyType
    {
        Field, Serial, Ref
    }
    
    public enum CacheConfigType
    {
        Auto, Manual
    }
    
    /**
     * whether using cache
     */
    private boolean useCache = false; 
    
    /**
     * Global cache type
     */
    private CacheConfigType cacheConfigType = CacheConfigType.Manual;
    
    /**
     * Default auto-cache id
     */
    private String autoCacheId; 
    
    /**
     * Whether cache data synchronous
     */
    private boolean synchronous = false; 
    
    private CacheConfigData appCacheData = null;
    
    private CacheConfigData threadCacheData = null;
    
    private CacheConfigData sessionCacheData = null;
    
    private ConcurrentMap<String, CacheConfigData> cacheMap = null;
    
    public CacheConfiguration()
    {
        cacheMap = new ConcurrentHashMap<String, CacheConfigData>();
    }
    
    public void readCacheConfig(Element element)
    {
        if (element == null)
            return;
        for (Iterator<Element> it = element.elementIterator(); it.hasNext();)
        {
            try
            {
                Element el = it.next();
                String name = el.getName().trim();
                if ("AppCache".equalsIgnoreCase(name))
                {
                    readAppCacheConfig(el);
                }
                else if ("ThreadCache".equalsIgnoreCase(name))
                {
                    readThreadCacheConfig(el);
                }
                else if ("EHCache".equalsIgnoreCase(name))
                {
                    readEHCacheConfig(el);
                }
            }
            catch (Exception e)
            {
            }
        }
    }
    
    public void readAppCacheConfig(Element element)
        throws Exception
    {
        if (element == null)
            return;
        appCacheData = new CacheConfigData();
        appCacheData.setCacheEnumType(CacheScopeType.Application);
        appCacheData.setId("application");
        for (Iterator<Attribute> it = element.attributeIterator(); it.hasNext();)
        {
            Attribute at = it.next();
            String name = at.getName().trim();
            String value = at.getValue().trim();
            if ("id".equalsIgnoreCase(name))
            {
                if (value != null && !"".equals(value))
                    appCacheData.setId(value);
            }
            else if ("strategy".equalsIgnoreCase(name))
            {
                if ("Fifo".equalsIgnoreCase(value))
                {
                    appCacheData.setCacheStrategy(CacheControlStrategy.Fifo);
                }
                else if ("Lru".equalsIgnoreCase(value))
                {
                    appCacheData.setCacheStrategy(CacheControlStrategy.Lru);
                }
                else if ("SoftRef".equalsIgnoreCase(value))
                {
                    appCacheData.setCacheStrategy(CacheControlStrategy.SoftRef);
                }
                else if ("WeakRef".equalsIgnoreCase(value))
                {
                    appCacheData.setCacheStrategy(CacheControlStrategy.WeakRef);
                }
            }
            else if ("maxObject".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                appCacheData.setMaxObject(Integer.parseInt(value));
            }
            else if ("eternal".equalsIgnoreCase(name))
            {
                if ("false".equalsIgnoreCase(value))
                {
                    appCacheData.setEternal(false);
                }
                else
                {
                    appCacheData.setEternal(true);
                }
            }
            else if ("entirety".equalsIgnoreCase(name))
            {
                if ("false".equalsIgnoreCase(value))
                {
                    appCacheData.setEntirety(false);
                }
                else
                {
                    appCacheData.setEntirety(true);
                }
            }
            else if ("idleTime".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                appCacheData.setIdleTime(Integer.parseInt(value) * 1000);
            }
            else if ("liveTime".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                appCacheData.setLiveTime(Integer.parseInt(value) * 1000);
            }
            else if ("copyStrategy".equalsIgnoreCase(name))
            {
                value = value.toLowerCase();
                if ("field".equalsIgnoreCase(value))
                    appCacheData.setCopyStrategyType(CopyStrategyType.Field);
                else if ("serial".equalsIgnoreCase(value))
                    appCacheData.setCopyStrategyType(CopyStrategyType.Serial);
                else if ("ref".equalsIgnoreCase(value))
                    appCacheData.setCopyStrategyType(CopyStrategyType.Ref);
            }
        }
        cacheMap.put(appCacheData.getId(), appCacheData);
    }
    
    public void readThreadCacheConfig(Element element)
        throws Exception
    {
        if (element == null)
        {
            return;
        }
        threadCacheData = new CacheConfigData();
        threadCacheData.setCacheEnumType(CacheScopeType.Thread);
        threadCacheData.setId("thread");
        for (Iterator<Attribute> it = element.attributeIterator(); it.hasNext();)
        {
            Attribute at = it.next();
            String name = at.getName().trim();
            String value = at.getValue().trim();
            if ("id".equalsIgnoreCase(name))
            {
                if (value != null && !"".equals(value))
                    threadCacheData.setId(value);
            }
            else if ("strategy".equalsIgnoreCase(name))
            {
                if ("Fifo".equalsIgnoreCase(value))
                {
                    threadCacheData.setCacheStrategy(CacheControlStrategy.Fifo);
                }
                else if ("Lru".equalsIgnoreCase(value))
                {
                    threadCacheData.setCacheStrategy(CacheControlStrategy.Lru);
                }
                else if ("SoftRef".equalsIgnoreCase(value))
                {
                    threadCacheData.setCacheStrategy(CacheControlStrategy.SoftRef);
                }
                else if ("WeakRef".equalsIgnoreCase(value))
                {
                    threadCacheData.setCacheStrategy(CacheControlStrategy.WeakRef);
                }
            }
            else if ("maxObject".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                threadCacheData.setMaxObject(Integer.parseInt(value));
            }
            else if ("entirety".equalsIgnoreCase(name))
            {
                if ("false".equalsIgnoreCase(value))
                {
                    threadCacheData.setEntirety(false);
                }
                else
                {
                    threadCacheData.setEntirety(true);
                }
            }
            else if ("copyStrategy".equalsIgnoreCase(name))
            {
                value = value.toLowerCase();
                if ("field".equalsIgnoreCase(value))
                {
                    threadCacheData.setCopyStrategyType(CopyStrategyType.Field);
                }
                else if ("serial".equalsIgnoreCase(value))
                {
                    threadCacheData.setCopyStrategyType(CopyStrategyType.Serial);
                }
                else if ("ref".equalsIgnoreCase(value))
                {
                    threadCacheData.setCopyStrategyType(CopyStrategyType.Ref);
                }
            }
        }
        cacheMap.put(threadCacheData.getId(), threadCacheData);
    }
    
    public void readSessionCacheConfig(Element element)
        throws Exception
    {
        if (element == null)
            return;
        sessionCacheData = new CacheConfigData();
        sessionCacheData.setCacheEnumType(CacheScopeType.Session);
        sessionCacheData.setId("session");
        for (Iterator<Attribute> it = element.attributeIterator(); it.hasNext();)
        {
            Attribute at = it.next();
            String name = at.getName().trim();
            String value = at.getValue().trim();
            if ("id".equalsIgnoreCase(name))
            {
                if (value != null && !"".equals(value))
                    sessionCacheData.setId(value);
            }
            else if ("strategy".equalsIgnoreCase(name))
            {
                if ("Fifo".equalsIgnoreCase(value))
                {
                    sessionCacheData.setCacheStrategy(CacheControlStrategy.Fifo);
                }
                else if ("Lru".equalsIgnoreCase(value))
                {
                    sessionCacheData.setCacheStrategy(CacheControlStrategy.Lru);
                }
                else if ("SoftRef".equalsIgnoreCase(value))
                {
                    sessionCacheData.setCacheStrategy(CacheControlStrategy.SoftRef);
                }
                else if ("WeakRef".equalsIgnoreCase(value))
                {
                    sessionCacheData.setCacheStrategy(CacheControlStrategy.WeakRef);
                }
            }
            else if ("maxObject".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                sessionCacheData.setMaxObject(Integer.parseInt(value));
            }
            else if ("entirety".equalsIgnoreCase(name))
            {
                if ("false".equalsIgnoreCase(value))
                {
                    sessionCacheData.setEntirety(false);
                }
                else
                {
                    sessionCacheData.setEntirety(true);
                }
            }
            else if ("copyStrategy".equalsIgnoreCase(name))
            {
                value = value.toLowerCase();
                if ("field".equalsIgnoreCase(value))
                    sessionCacheData.setCopyStrategyType(CopyStrategyType.Field);
                else if ("serial".equalsIgnoreCase(value))
                    sessionCacheData.setCopyStrategyType(CopyStrategyType.Serial);
                else if ("ref".equalsIgnoreCase(value))
                    sessionCacheData.setCopyStrategyType(CopyStrategyType.Ref);
            }
        }
        cacheMap.put(sessionCacheData.getId(), sessionCacheData);
    }
    
    public void readEHCacheConfig(Element element)
        throws Exception
    {
        if (element == null)
            return;
        CacheConfigData ehCacheData = new CacheConfigData();
        ehCacheData.setCacheEnumType(CacheScopeType.EHCache);
        for (Iterator<Attribute> it = element.attributeIterator(); it.hasNext();)
        {
            Attribute at = it.next();
            String name = at.getName().trim();
            String value = at.getValue().trim();
            if ("id".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    throw new Exception("EHCache Config id is null");
                ehCacheData.setId(value);
            }
            if ("name".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    throw new Exception("EHCache Config name is null");
                ehCacheData.setId(value);
            }
            if ("configPath".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                ehCacheData.setEhcacheConfigPath(value);
            }
            else if ("maxElementsInMemory".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                ehCacheData.setMaxObject(Integer.parseInt(value));
            }
            else if ("maxElementsOnDisk".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                ehCacheData.setMaxElementsOnDisk(Integer.parseInt(value));
            }
            else if ("eternal".equalsIgnoreCase(name))
            {
                if ("false".equalsIgnoreCase(value))
                {
                    ehCacheData.setEternal(false);
                }
                else
                {
                    ehCacheData.setEternal(true);
                }
            }
            else if ("timeToIdleSeconds".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                ehCacheData.setIdleTime(Integer.parseInt(value));
            }
            else if ("timeToLiveSeconds".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                ehCacheData.setLiveTime(Integer.parseInt(value));
            }
            else if ("overflowToDisk".equalsIgnoreCase(name))
            {
                if ("false".equalsIgnoreCase(value))
                {
                    ehCacheData.setOverflowToDisk(false);
                }
                else
                {
                    ehCacheData.setOverflowToDisk(true);
                }
            }
            else if ("diskPersistent".equalsIgnoreCase(name))
            {
                if ("false".equalsIgnoreCase(value))
                {
                    ehCacheData.setDiskPersistent(false);
                }
                else
                {
                    ehCacheData.setDiskPersistent(true);
                }
            }
            else if ("diskExpiryThreadIntervalSeconds".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                ehCacheData.setDiskExpiryThreadIntervalSeconds(Integer.parseInt(value));
            }
            else if ("memoryStoreEvictionPolicy".equalsIgnoreCase(name))
            {
                if (value == null || "".equals(value))
                    continue;
                ehCacheData.setMemoryStoreEvictionPolicy(value);
            }
        }
        cacheMap.put(ehCacheData.getId(), ehCacheData);
    }
    
    public CacheConfigData getAppCacheData()
    {
        return appCacheData;
    }
    
    public void setAppCacheData(CacheConfigData appCacheData)
    {
        this.appCacheData = appCacheData;
    }
    
    public CacheConfigData getThreadCacheData()
    {
        return threadCacheData;
    }
    
    public void setThreadCacheData(CacheConfigData threadCacheData)
    {
        this.threadCacheData = threadCacheData;
    }
    
    public ConcurrentMap<String, CacheConfigData> getCacheMap()
    {
        return cacheMap;
    }
    
    public CacheConfigData getSessionCacheData()
    {
        return sessionCacheData;
    }
    
    public boolean isUseCache()
    {
        return useCache;
    }
    
    public CacheConfigType getCacheConfigType()
    {
        return cacheConfigType;
    }
    
    public String getAutoCacheId()
    {
        return autoCacheId;
    }
    
    public boolean isSynchronous()
    {
        return synchronous;
    }
    
    public void setUseCache(boolean useCache)
    {
        this.useCache = useCache;
    }
    
    public void setCacheConfigType(CacheConfigType cacheConfigType)
    {
        this.cacheConfigType = cacheConfigType;
    }
    
    public void setAutoCacheId(String autoCacheId)
    {
        this.autoCacheId = autoCacheId;
    }
    
    public void setSynchronous(boolean synchronous)
    {
        this.synchronous = synchronous;
    }
    
}