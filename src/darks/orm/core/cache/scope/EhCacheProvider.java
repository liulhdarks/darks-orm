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

package darks.orm.core.cache.scope;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import darks.orm.core.cache.CacheContext;
import darks.orm.core.config.CacheConfiguration;
import darks.orm.core.config.Configuration;
import darks.orm.core.data.xml.CacheConfigData;
import darks.orm.core.session.SessionContext;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@SuppressWarnings("unchecked")
public class EhCacheProvider implements CacheProvider
{
    
    private CacheManager manager;
    
    private boolean vaild = false;
    
    private CacheContext cacheContext;
    
    public EhCacheProvider()
    {
        
    }
    
    /**
     * 初始化EHCACHE
     */
    public boolean initialize(CacheContext cacheContext)
    {
        this.cacheContext = cacheContext;
        if (!isClassLoaded())
            return false;
        Configuration cfg = SessionContext.getConfigure();
        if (cfg == null)
            return false;
        CacheConfiguration cacheCfg = cfg.getCacheConfig();
        if (cacheCfg == null)
            return false;
        ConcurrentMap<String, CacheConfigData> caches = cacheCfg.getCacheMap();
        for (Entry<String, CacheConfigData> entry : caches.entrySet())
        {
            CacheConfigData data = entry.getValue();
            if (data.getCacheEnumType() != CacheScopeType.EHCache)
                continue;
            if (data.getEhcacheConfigPath() != null && !"".equals(data.getEhcacheConfigPath()))
            {
                manager = new CacheManager(data.getEhcacheConfigPath());
                String[] names = manager.getCacheNames();
                for (String name : names)
                {
                    Cache cache = manager.getCache(name);
                    if (cache == null)
                        continue;
                    CacheFactory cacheFactory = new EhCacheFactory(this, cache);
                    cacheContext.addCacheFactory(name, cacheFactory);
                }
                break;
            }
        }
        
        if (manager == null)
        {
            manager = new CacheManager();
            for (Entry<String, CacheConfigData> entry : caches.entrySet())
            {
                String cacheId = entry.getKey();
                CacheConfigData data = entry.getValue();
                if (data.getCacheEnumType() != CacheScopeType.EHCache)
                    continue;
                net.sf.ehcache.config.CacheConfiguration cacheConfig =
                    new net.sf.ehcache.config.CacheConfiguration(cacheId, data.getMaxObject()).overflowToDisk(data.isOverflowToDisk())
                        .eternal(data.isEternal())
                        .timeToLiveSeconds(data.getLiveTime())
                        .timeToIdleSeconds(data.getIdleTime())
                        .diskPersistent(data.isDiskPersistent())
                        .diskExpiryThreadIntervalSeconds(data.getDiskExpiryThreadIntervalSeconds())
                        .maxElementsOnDisk(data.getMaxElementsOnDisk())
                        .memoryStoreEvictionPolicy(data.getMemoryStoreEvictionPolicy());
                Cache cache = new Cache(cacheConfig);
                manager.addCache(cache);
                CacheFactory cacheFactory = new EhCacheFactory(this, cache);
                cacheContext.addCacheFactory(cacheId, cacheFactory);
            }
        }
        if (manager != null)
            vaild = true;
        return vaild;
    }
    
    /**
     * ehcache jar包是否已经包含
     * 
     * @return
     */
    public boolean isClassLoaded()
    {
        try
        {
            Class c = Class.forName("net.sf.ehcache.CacheManager");
            if (c != null)
                return true;
            return false;
        }
        catch (ClassNotFoundException e)
        {
            return false;
        }
    }
    
    public void shutdown()
    {
        Configuration cfg = SessionContext.getConfigure();
        if (cfg == null)
            return;
        CacheConfiguration cacheCfg = cfg.getCacheConfig();
        if (cacheCfg == null)
            return;
        ConcurrentMap<String, CacheConfigData> caches = cacheCfg.getCacheMap();
        for (Entry<String, CacheConfigData> entry : caches.entrySet())
        {
            String cacheId = entry.getKey();
            CacheConfigData data = entry.getValue();
            if (data.getCacheEnumType() != CacheScopeType.EHCache)
                continue;
            cacheContext.removeCacheFactory(cacheId);
        }
        if (manager != null)
        {
            manager.shutdown();
        }
    }
    
    public boolean isInit()
    {
        return vaild;
    }
    
    public CacheManager getManager()
    {
        return manager;
    }
    
    public boolean isVaild()
    {
        return vaild;
    }
    
    public CacheContext getCacheContext()
    {
        return cacheContext;
    }
    
}