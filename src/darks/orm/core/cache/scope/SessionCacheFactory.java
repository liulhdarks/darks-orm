package darks.orm.core.cache.scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpSession;

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
import darks.orm.web.context.RequestContext;

@SuppressWarnings("unchecked")
public class SessionCacheFactory implements CacheFactory
{
    
    private static final Logger logger = LoggerFactory.getLogger(SessionCacheFactory.class);
    
    private static final String SESSION_KEYLIST_KEY = "DARKS$SESSION$KEYLIST_KEY";
    
    private static final String SESSION_ENTITYMAP_KEY = "DARKS$SESSION$ENTITYMAP_KEY";
    
    private static final String SESSION_ENTITYLISTMAP_KEY = "DARKS$SESSION$ENTITYLISTMAP_KEY";
    
    private static final String SESSION_CONTROLLER_KEY = "DARKS$SESSION$CONTROLLER_KEY";
    
    private static volatile SessionCacheFactory instace = null;
    
    private static final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    
    private static final Lock rlock = rwlock.readLock();
    
    private static final Lock wlock = rwlock.writeLock();
    
    private CacheConfigData sessionConfigData;
    
    private CopyStrategy copyStrategy;
    
    private SessionCacheFactory()
    {
        try
        {
            Configuration cfg = SessionContext.getConfigure();
            CacheConfiguration cacheCfg = cfg.getCacheConfig();
            if (cacheCfg == null)
                return;
            sessionConfigData = cacheCfg.getSessionCacheData();
            if (sessionConfigData == null)
                return;
            copyStrategy = sessionConfigData.getCopyStrategy();
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
            instace = new SessionCacheFactory();
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
        
        wlock.lock();
        try
        {
            if (obj instanceof List && !sessionConfigData.isEntirety())
            {
                List list = (List)obj;
                if (list.size() > sessionConfigData.getMaxObject())
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
        finally
        {
            wlock.unlock();
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
        rlock.lock();
        try
        {
            
            if ((key.getCacheKeyType() == CacheKeyType.ListKey || key.getCacheKeyType() == CacheKeyType.PageKey)
                && !sessionConfigData.isEntirety())
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
        finally
        {
            rlock.unlock();
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
        rlock.lock();
        try
        {
            if ((key.getCacheKeyType() == CacheKeyType.ListKey || key.getCacheKeyType() == CacheKeyType.PageKey)
                && !sessionConfigData.isEntirety())
            {
                return listMap.containsKey(key);
            }
            return entityMap.containsKey(key);
        }
        finally
        {
            rlock.unlock();
        }
    }
    
    /**
     * 获得键值列表
     * 
     * @return 键值队列
     */
    private Queue<CacheKey> getKeysList()
    {
        HttpSession session = RequestContext.getInstance().getSession();
        Queue<CacheKey> keysList = (Queue<CacheKey>)session.getAttribute(SESSION_KEYLIST_KEY);
        if (keysList == null)
        {
            keysList = new ConcurrentLinkedQueue<CacheKey>();
            session.setAttribute(SESSION_KEYLIST_KEY, keysList);
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
        HttpSession session = RequestContext.getInstance().getSession();
        Map<CacheKey, Object> entityMap = (Map<CacheKey, Object>)session.getAttribute(SESSION_ENTITYMAP_KEY);
        if (entityMap == null)
        {
            int initnum = 0;
            int max = sessionConfigData.getMaxObject();
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
            session.setAttribute(SESSION_ENTITYMAP_KEY, entityMap);
        }
        return entityMap;
    }
    
    private Map<CacheKey, CacheList> getListMap()
    {
        HttpSession session = RequestContext.getInstance().getSession();
        Map<CacheKey, CacheList> listMap = (Map<CacheKey, CacheList>)session.getAttribute(SESSION_ENTITYLISTMAP_KEY);
        if (listMap == null)
        {
            listMap = new ConcurrentHashMap<CacheKey, CacheList>(64);
            session.setAttribute(SESSION_ENTITYLISTMAP_KEY, listMap);
        }
        return listMap;
    }
    
    private CacheController getCacheController()
    {
        
        HttpSession session = RequestContext.getInstance().getSession();
        CacheController ctrl = (CacheController)session.getAttribute(SESSION_CONTROLLER_KEY);
        if (ctrl == null)
        {
            Queue<CacheKey> keysList = getKeysList();
            Map<CacheKey, Object> entityMap = getEntityMap();
            Map<CacheKey, CacheList> listMap = getListMap();
            ctrl = CacheControllerFactroy.getCacheController(sessionConfigData, keysList, entityMap, listMap);
            session.setAttribute(SESSION_CONTROLLER_KEY, ctrl);
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
        wlock.lock();
        try
        {
            listMap.clear();
            keysList.clear();
            entityMap.clear();
        }
        catch (Exception e)
        {
            
        }
        finally
        {
            wlock.unlock();
        }
        
    }
}
