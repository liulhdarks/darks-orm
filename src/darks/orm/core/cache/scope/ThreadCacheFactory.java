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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import net.sf.ehcache.CacheException;
import darks.orm.core.cache.CacheContext.CacheKeyType;
import darks.orm.core.cache.CacheController;
import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheList;
import darks.orm.core.cache.CacheObject;
import darks.orm.core.cache.control.CacheControllerFactroy;
import darks.orm.core.cache.strategy.CopyStrategy;
import darks.orm.core.config.CacheConfiguration;
import darks.orm.core.config.Configuration;
import darks.orm.core.data.EntityData;
import darks.orm.core.data.FieldData;
import darks.orm.core.data.xml.CacheConfigData;
import darks.orm.core.session.SessionContext;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.ByteHelper;
import darks.orm.util.ThreadHelper;

public class ThreadCacheFactory implements CacheFactory
{
    
    private static final Logger logger = LoggerFactory.getLogger(ThreadCacheFactory.class);
    
    private static volatile ThreadCacheFactory instace = null;
    
    private static final ThreadLocal<Queue<CacheKey>> threadKeysList = new ThreadLocal<Queue<CacheKey>>();
    
    private static final ThreadLocal<Map<CacheKey, Object>> threadEntityMap = new ThreadLocal<Map<CacheKey, Object>>();
    
    private static final ThreadLocal<Map<CacheKey, CacheList>> threadListMap =
        new ThreadLocal<Map<CacheKey, CacheList>>();
    
    private static final ThreadLocal<CacheController> threadController = new ThreadLocal<CacheController>();
    
    private static final ConcurrentMap<Long, Thread> threadMap = new ConcurrentHashMap<Long, Thread>(256);
    
    private CacheConfigData threadConfigData;
    
    private CopyStrategy copyStrategy;
    
    private ThreadCacheFactory()
    {
        try
        {
            Configuration cfg = SessionContext.getConfigure();
            CacheConfiguration cacheCfg = cfg.getCacheConfig();
            if (cacheCfg == null)
                return;
            threadConfigData = cacheCfg.getThreadCacheData();
            if (threadConfigData == null)
                return;
            copyStrategy = threadConfigData.getCopyStrategy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static CacheFactory getInstance()
    {
        Configuration cfg = SessionContext.getConfigure();
        if (cfg == null)
            return null;
        CacheConfiguration cacheCfg = cfg.getCacheConfig();
        if (cacheCfg == null)
            return null;
        CacheConfigData data = cacheCfg.getThreadCacheData();
        if (data == null)
            return null;
        if (instace == null)
        {
            instace = new ThreadCacheFactory();
        }
        return instace;
    }
    
    /**
     * 缓存对象
     * 
     * @param key 缓存KEY
     * @param obj 对象实例
     * @throws Exception
     */
    public void cacheObject(CacheKey key, Object obj)
        throws Exception
    {
        if (key == null || obj == null)
            return;
        EntityData data = key.getData();
        if (data == null || !ByteHelper.isSerializable(data))
            return;
        
        Map<CacheKey, CacheList> listMap = getListMap();
        CacheController controller = getCacheController();
        Thread t = Thread.currentThread();
        if (!threadMap.containsKey(t.getId()))
        {
            threadMap.put(t.getId(), t);
        }
        try
        {
            if (obj instanceof List && !threadConfigData.isEntirety())
            {
                List list = (List)obj;
                if (list.size() > threadConfigData.getMaxObject())
                {
                    throw new CacheException("the size of list cacheing is flowover the max limit");
                }
                FieldData pkfdata = data.getPkField();
                Map<CacheKey, Object> map = new ConcurrentHashMap<CacheKey, Object>();
                List<CacheKey> clist = new ArrayList<CacheKey>();
                for (Object ob : list)
                {
                    int piId = (Integer)pkfdata.getValue(ob);
                    CacheKey newkey = new CacheKey(data, piId, CacheKeyType.SingleKey);
                    Object value = new CacheObject(copyStrategy, newkey, ob);// new
                                                                             // SoftReference<Object>(ob);//(Object)ByteHelper.ObjectToByte(ob);
                    controller.cacheObject(newkey, value);
                    map.put(newkey, value);
                    clist.add(newkey);
                }
                CacheList cacheList = new CacheList(map, clist);
                listMap.put(key, cacheList);
            }
            else
            {
                Object value = new CacheObject(copyStrategy, key, obj);// new
                                                                       // SoftReference<Object>(obj);//(Object)ByteHelper.ObjectToByte(obj);
                controller.cacheObject(key, value);
            }
        }
        catch (Exception e)
        {
            throw new CacheException(e.getMessage(), e);
        }
    }
    
    /**
     * 获得对象
     * 
     * @param key 缓存KEY
     * @return 对象实例
     * @throws Exception
     */
    public Object getObject(CacheKey key)
        throws Exception
    {
        if (key.getData() == null)
            return false;
        if (!key.getData().isSerializable())
            return false;
        
        Map<CacheKey, CacheList> listMap = getListMap();
        CacheController controller = getCacheController();
        
        try
        {
            
            if ((key.getCacheKeyType() == CacheKeyType.ListKey || key.getCacheKeyType() == CacheKeyType.PageKey)
                && !threadConfigData.isEntirety())
            {
                CacheList cacheList = listMap.get(key);
                if (cacheList == null)
                    return null;
                key.setCount(cacheList.getCount());
                List<CacheKey> clist = cacheList.getList();
                List<Object> list = new ArrayList<Object>(clist.size());
                for (CacheKey ckey : clist)
                {
                    CacheObject val = (CacheObject)controller.getObject(ckey);
                    if (val == null)
                        return null;
                    val.setLastIdleTime(System.currentTimeMillis());
                    list.add(val.getObject());
                }
                return list;
            }
            
            CacheObject value = (CacheObject)controller.getObject(key);
            if (value == null)
                return null;
            key.setCount(value.getKey().getCount());
            value.setLastIdleTime(System.currentTimeMillis());
            return value.getObject();
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    /**
     * 缓存中是否存在拥有此缓存KEY的对象
     * 
     * @param key 缓存KEY
     * @return true存在 false不存在
     */
    public boolean containKey(CacheKey key)
    {
        Map<CacheKey, Object> entityMap = getEntityMap();
        Map<CacheKey, CacheList> listMap = getListMap();
        if ((key.getCacheKeyType() == CacheKeyType.ListKey || key.getCacheKeyType() == CacheKeyType.PageKey)
            && !threadConfigData.isEntirety())
        {
            return listMap.containsKey(key);
        }
        return entityMap.containsKey(key);
    }
    
    /**
     * 获得键值列表
     * 
     * @return 键值队列
     */
    private Queue<CacheKey> getKeysList()
    {
        Queue<CacheKey> keysList = threadKeysList.get();
        if (keysList == null)
        {
            keysList = new ConcurrentLinkedQueue<CacheKey>();
            threadKeysList.set(keysList);
        }
        return keysList;
    }
    
    /**
     * 获得实体MAP
     * 
     * @return MAP
     */
    private Map<CacheKey, Object> getEntityMap()
    {
        Map<CacheKey, Object> entityMap = threadEntityMap.get();
        if (entityMap == null)
        {
            int initnum = 0;
            int max = threadConfigData.getMaxObject();
            if (max < 1000)
            {
                initnum = max * 2 / 3;
            }
            else if (max < 10000)
            {
                initnum = max * 1 / 5;
            }
            else if (max > 10000)
            {
                initnum = max * 1 / 100;
            }
            entityMap = new ConcurrentHashMap<CacheKey, Object>(initnum);
            threadEntityMap.set(entityMap);
        }
        return entityMap;
    }
    
    private Map<CacheKey, CacheList> getListMap()
    {
        Map<CacheKey, CacheList> listMap = threadListMap.get();
        if (listMap == null)
        {
            listMap = new ConcurrentHashMap<CacheKey, CacheList>(64);
            threadListMap.set(listMap);
        }
        return listMap;
    }
    
    private CacheController getCacheController()
    {
        CacheController ctrl = threadController.get();
        if (ctrl == null)
        {
            Queue<CacheKey> keysList = getKeysList();
            Map<CacheKey, Object> entityMap = getEntityMap();
            Map<CacheKey, CacheList> listMap = getListMap();
            ctrl = CacheControllerFactroy.getCacheController(threadConfigData, keysList, entityMap, listMap);
            threadController.set(ctrl);
        }
        return ctrl;
    }
    
    public void debug()
    {
        Queue<CacheKey> keysList = getKeysList();
        Map<CacheKey, Object> entityMap = getEntityMap();
        Map<CacheKey, CacheList> listMap = getListMap();
        System.out.println(keysList.size() + " " + entityMap.size() + " " + listMap.size());
        for (CacheKey key : keysList)
        {
            System.out.println(key.getId() + " " + key.getCacheKeyType());
        }
    }
    
    public void flush()
    {
        Queue<CacheKey> keysList = getKeysList();
        Map<CacheKey, Object> entityMap = getEntityMap();
        Map<CacheKey, CacheList> listMap = getListMap();
        try
        {
            listMap.clear();
            keysList.clear();
            entityMap.clear();
            cleanThreadLocals();
        }
        catch (Exception e)
        {
            
        }
        
    }
    
    private void cleanThreadLocals()
        throws NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
    {
        try
        {
            for (Thread t : threadMap.values())
            {
                ThreadHelper.cleanThreadLocals(t);
            }
            threadMap.clear();
        }
        finally
        {
        }
    }
    
}