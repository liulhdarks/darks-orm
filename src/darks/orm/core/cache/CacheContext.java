package darks.orm.core.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import darks.orm.app.Page;
import darks.orm.core.cache.scope.ApplicationCacheFactory;
import darks.orm.core.cache.scope.CacheFactory;
import darks.orm.core.cache.scope.CacheProvider;
import darks.orm.core.cache.scope.EhCacheProvider;
import darks.orm.core.cache.scope.ThreadCacheFactory;
import darks.orm.core.cache.thread.CacheAsynchronousThread;
import darks.orm.core.data.EntityData;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.session.SessionContext;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.ThreadHelper;

/**
 * 类名:CacheContext 作者:刘力华 创建时间:2012.02.15 版本:1.0.2 alpha 版权:CopyRight(c)2012 刘力华
 * 该项目工程所有权归刘力华所有 描述:全局控制缓存上下文
 */
@SuppressWarnings("unchecked")
public class CacheContext
{
    
    private static Logger log = LoggerFactory.getLogger(CacheContext.class);
    
    public static final String SCOPE_APPLICATION = "application";
    
    public static final String SCOPE_THREAD = "thread";
    
    public enum CacheKeyType
    {
        SingleKey, ListKey, PageKey
    }
    
    private List<CacheProvider> cacheProviderList = new ArrayList<CacheProvider>();
    
    private ConcurrentMap<String, CacheFactory> cacheFactorys = null;
    
    private boolean synchronous = true;
    
    public CacheContext()
    {
        cacheFactorys = new ConcurrentHashMap<String, CacheFactory>(3);
        addCacheFactory(SCOPE_APPLICATION, ApplicationCacheFactory.getInstance());
        addCacheFactory(SCOPE_THREAD, ThreadCacheFactory.getInstance());
        EhCacheProvider ehCacheProvider = new EhCacheProvider();
        if (ehCacheProvider.isClassLoaded())
        {
            addCacheProvider(ehCacheProvider);
        }
        synchronous = SessionContext.getConfigure().getCacheConfig().isSynchronous();
    }
    
    /**
     * 初始化缓存上下文
     */
    public void initialize()
    {
        for (CacheProvider cacheProvider : cacheProviderList)
        {
            if (cacheProvider.isClassLoaded() && !cacheProvider.isInit())
            {
                cacheProvider.initialize(this);
            }
        }
    }
    
    public void shutdown()
    {
        for (CacheProvider cacheProvider : cacheProviderList)
        {
            if (cacheProvider.isClassLoaded() && cacheProvider.isInit())
            {
                cacheProvider.shutdown();
            }
        }
    }
    
    /**
     * 添加缓存工厂
     * 
     * @param key 工厂键值
     * @param cacheFactory 工厂实例
     */
    public void addCacheFactory(String key, CacheFactory cacheFactory)
    {
        cacheFactorys.put(key, cacheFactory);
    }
    
    /**
     * 移除缓存工厂
     * 
     * @param key 工厂键值
     */
    public void removeCacheFactory(String key)
    {
        cacheFactorys.remove(key);
    }
    
    /**
     * 添加缓存供应者
     * 
     * @param cacheProvider
     */
    public void addCacheProvider(CacheProvider cacheProvider)
    {
        cacheProviderList.add(cacheProvider);
    }
    
    /**
     * 缓存单个对象
     * 
     * @param cls 实体类
     * @param cacheId 缓存名称
     * @param id 实体主键值
     * @param value 实体实例
     */
    public <T> void cacheSingle(Class<T> cls, String cacheId, int id, Object value, boolean cascade)
    {
        try
        {
            EntityData data = ClassFactory.getEntity(cls.getName());
            CacheKey key = new CacheKey(data, id, CacheKeyType.SingleKey);
            key.setCascade(cascade);
            cacheObject(key, cacheId, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 缓存单个对象
     * 
     * @param cls 实体类
     * @param cacheId 缓存名称
     * @param sql SQL语句
     * @param params 注入数组参数
     * @param value 实体实例
     */
    public <T> void cacheSingle(Class<T> cls, String cacheId, String sql, Object[] params, Object value, boolean cascade)
    {
        try
        {
            EntityData data = ClassFactory.getEntity(cls.getName());
            CacheKey key = new CacheKey(data, sql, params, CacheKeyType.SingleKey);
            key.setCascade(cascade);
            cacheObject(key, cacheId, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 缓存实体列表
     * 
     * @param cls 实体类
     * @param cacheId 缓存名称
     * @param sql SQL语句
     * @param params 注入数组参数
     * @param value 实体实例
     */
    public <T> void cacheList(Class<T> cls, String cacheId, String sql, Object[] params, Object value, boolean cascade)
    {
        try
        {
            EntityData data = ClassFactory.getEntity(cls.getName());
            CacheKey key = new CacheKey(data, sql, params, CacheKeyType.ListKey);
            key.setCascade(cascade);
            cacheObject(key, cacheId, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 缓存分页对象
     * 
     * @param cls 实体类
     * @param cacheId 缓存名称
     * @param sql SQL语句
     * @param params 注入数组参数
     * @param page 当前页数
     * @param pageSize 分页数
     * @param count 记录总数
     * @param value 实体实例
     */
    public <T> void cachePage(Class<T> cls, String cacheId, String sql, Object[] params, int page, int pageSize,
        int count, Object value, boolean cascade)
    {
        try
        {
            EntityData data = ClassFactory.getEntity(cls.getName());
            CacheKey key = new CacheKey(sql, params, page, pageSize, data, CacheKeyType.PageKey, count);
            key.setCascade(cascade);
            cacheObject(key, cacheId, value);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 缓存对象
     * 
     * @param key 缓存KEY
     * @param cacheId 缓存名称
     * @param value 对象实例
     * @throws Exception
     */
    private void cacheObject(CacheKey key, String cacheId, Object value)
        throws Exception
    {
        CacheFactory factory = cacheFactorys.get(cacheId);
        if (factory != null)
        {
            if (synchronous)
            {
                factory.cacheObject(key, value);
            }
            else
            {
                ThreadHelper.addThread(new CacheAsynchronousThread(factory, key, cacheId, value));
            }
        }
    }
    
    /**
     * 获得缓存对象
     * 
     * @param key 缓存KEY
     * @param cacheId 缓存名称
     * @return 缓存对象
     * @throws Exception
     */
    public Object getObject(CacheKey key, String cacheId)
        throws Exception
    {
        CacheFactory factory = cacheFactorys.get(cacheId);
        if (factory != null)
        {
            return factory.getObject(key);
        }
        return null;
    }
    
    /**
     * 获得单个对象
     * 
     * @param cls 实体类
     * @param cacheId 缓存名称
     * @param id 主键值
     * @return 单个对象
     */
    public <T> T getSingle(Class<T> cls, String cacheId, int id, boolean cascade)
    {
        try
        {
            EntityData data = ClassFactory.parseClass(cls);
            CacheKey key = new CacheKey(data, id, CacheKeyType.SingleKey);
            key.setCascade(cascade);
            return (T)getObject(key, cacheId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public <T> T getSingle(Class<T> cls, String cacheId, String sql, Object[] params, boolean cascade)
    {
        try
        {
            EntityData data = ClassFactory.parseClass(cls);
            CacheKey key = new CacheKey(data, sql, params, CacheKeyType.SingleKey);
            key.setCascade(cascade);
            return (T)getObject(key, cacheId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public <T> List<T> getList(Class<T> cls, String cacheId, String sql, Object[] params, boolean cascade)
    {
        try
        {
            EntityData data = ClassFactory.parseClass(cls);
            CacheKey key = new CacheKey(data, sql, params, CacheKeyType.ListKey);
            key.setCascade(cascade);
            return (List<T>)getObject(key, cacheId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public <T> Page<T> getPage(Class<T> cls, String cacheId, String sql, Object[] params, int page, int pageSize,
        boolean cascade)
    {
        try
        {
            EntityData data = ClassFactory.parseClass(cls);
            CacheKey key = new CacheKey(sql, params, page, pageSize, data, CacheKeyType.PageKey, 0);
            key.setCascade(cascade);
            List<T> list = (List<T>)getObject(key, cacheId);
            if (list == null)
                return null;
            Page<T> ret = new Page<T>(list, key.getCount());
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public void flushAll()
    {
        for (CacheFactory cacheFactory : cacheFactorys.values())
        {
            cacheFactory.flush();
        }
        log.debug("Cache Flush");
    }
    
    public boolean isSynchronous()
    {
        return synchronous;
    }
    
}
