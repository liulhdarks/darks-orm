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

package darks.orm.core.session;

import java.io.*;

import java.sql.ResultSet;
import java.util.List;

import darks.orm.app.Page;
import darks.orm.app.factory.GenericSqlMapFactory;
import darks.orm.app.factory.SqlMapFactory;
import darks.orm.core.cache.CacheContext;
import darks.orm.core.factory.ProxyBeanFactory;

/**
 * 
 * 
 * <p>
 * <h1>SqlSessionImpl.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public final class SqlSessionImpl extends SessionSupport implements Serializable
{
    
    private static final long serialVersionUID = 9127842587231565250L;
    
    private transient SqlMapFactory sqlMapFactory = null;
    
    public SqlSessionImpl()
    {
        sqlMapFactory = new GenericSqlMapFactory(this);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> List<T> queryCacheList(Class<T> c, String cacheId, String sql, Object... params)
    {
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && cacheId != null && ctx != null)
        {
            List<T> ret = ctx.getList(c, cacheId, sql, params, false);
            if (ret != null)
                return ret;
        }
        // ---------
        List<T> list = super.queryList(c, sql, params);
        // ----------
        if (SessionContext.isUseCache() && ctx != null)
        {
            ctx.cacheList(c, cacheId, sql, params, list, false);
        }
        return list;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> List<T> queryList(Class<T> c, ResultSet rs, String sql, Object... params)
    {
        String cacheId = SessionContext.getAutoCache();
        return this.queryList(c, cacheId, rs, sql, params);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> List<T> queryCacheList(Class<T> c, String cacheId, ResultSet rs, String sql, Object... params)
    {
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && cacheId != null && ctx != null)
        {
            List<T> ret = ctx.getList(c, cacheId, sql, params, false);
            if (ret != null)
                return ret;
        }
        // ---------
        List<T> list = super.queryList(c, rs, sql);
        // ----------
        if (SessionContext.isUseCache() && ctx != null)
        {
            ctx.cacheList(c, cacheId, sql, params, list, false);
        }
        return list;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryCascadeObject(Class<T> c, String sql, String[] entityName, String[] alias, Object... param)
    {
        String cacheId = SessionContext.getAutoCache();
        return this.queryCacheCascadeObject(c, cacheId, sql, entityName, alias, param);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryCascadeObject(Class<T> c, String sql, String entityNames, String aliases, Object... param)
    {
        String[] entityName = entityNames.split(",");
        String[] alias = aliases.split(",");
        return queryCascadeObject(c, sql, entityName, alias, param);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryCacheCascadeObject(Class<T> c, String cacheId, String sql, String[] entityName, String[] alias,
        Object... param)
    {
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && cacheId != null && ctx != null)
        {
            T ret = ctx.getSingle(c, cacheId, sql, param, true);
            if (ret != null)
                return ret;
        }
        // ---------
        T obj = super.queryCascadeObject(c, sql, entityName, alias, param);
        // ----------
        if (SessionContext.isUseCache() && ctx != null && cacheId != null)
        {
            ctx.cacheSingle(c, cacheId, sql, param, obj, true);
        }
        return obj;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> List<T> queryCascadeList(Class<T> c, String sql, String[] entityName, String[] alias, Object... param)
    {
        String cacheId = SessionContext.getAutoCache();
        return this.queryCacheCascadeList(c, cacheId, sql, entityName, alias, param);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> List<T> queryCascadeList(Class<T> c, String sql, String entityNames, String aliases, Object... param)
    {
        String[] entityName = entityNames.split(",");
        String[] alias = aliases.split(",");
        return queryCascadeList(c, sql, entityName, alias, param);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> List<T> queryCacheCascadeList(Class<T> c, String cacheId, String sql, String[] entityName, String[] alias,
        Object[] param)
    {
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && cacheId != null && ctx != null)
        {
            List<T> ret = ctx.getList(c, cacheId, sql, param, true);
            if (ret != null)
                return ret;
        }
        // ---------
        List<T> list = super.queryCascadeList(c, sql, entityName, alias, param);
        // ----------
        if (SessionContext.isUseCache() && ctx != null && cacheId != null)
        {
            ctx.cacheList(c, cacheId, sql, param, list, true);
        }
        return list;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> Page<T> queryCascadePageList(Class<T> c, String sql, int page, int pageSize, String[] entityName,
        String[] alias, Object... param)
    {
        String cacheId = SessionContext.getAutoCache();
        return this.queryCacheCascadePageList(c, cacheId, sql, page, pageSize, entityName, alias, param);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> Page<T> queryCascadePageList(Class<T> c, String sql, int page, int pageSize, String entityNames,
        String aliases, Object... param)
    {
        String[] entityName = entityNames.split(",");
        String[] alias = aliases.split(",");
        return queryCascadePageList(c, sql, page, pageSize, entityName, alias, param);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> Page<T> queryCacheCascadePageList(Class<T> c, String cacheId, String sql, int page, int pageSize,
        String[] entityName, String[] alias, Object... param)
    {
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && cacheId != null && ctx != null)
        {
            Page<T> ret = ctx.getPage(c, cacheId, sql, param, page, pageSize, true);
            if (ret != null)
                return ret;
        }
        // ---------
        Page<T> obj = super.queryCascadePageList(c, sql, page, pageSize, entityName, alias, param);
        // ----------
        if (SessionContext.isUseCache() && ctx != null && cacheId != null)
        {
            ctx.cachePage(c, cacheId, sql, param, page, pageSize, obj.getCount(), obj.getList(), true);
        }
        return obj;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryById(Class<T> c, int id)
    {
        String cacheId = SessionContext.getAutoCache();
        return this.queryCacheById(c, cacheId, id);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryCacheById(Class<T> c, String cacheId, int id)
    {
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && cacheId != null && ctx != null)
        {
            T ret = ctx.getSingle(c, cacheId, id, false);
            if (ret != null)
                return ret;
        }
        // ---------
        T obj = super.queryById(c, id);
        // ----------
        if (SessionContext.isUseCache() && ctx != null && cacheId != null)
        {
            ctx.cacheSingle(c, cacheId, id, obj, false);
        }
        return obj;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryBySQLA(Class<T> c, String sql, Object[] param)
    {
        String cacheId = SessionContext.getAutoCache();
        return this.queryCacheBySQL(c, cacheId, sql, param);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryCacheBySQL(Class<T> c, String cacheId, String sql, Object... param)
    {
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && cacheId != null && ctx != null)
        {
            T ret = ctx.getSingle(c, cacheId, sql, param, false);
            if (ret != null)
                return ret;
        }
        // ---------
        T obj = super.queryBySQL(c, sql, param);
        // ----------
        if (SessionContext.isUseCache() && ctx != null && cacheId != null)
        {
            ctx.cacheSingle(c, cacheId, sql, param, obj, false);
        }
        return obj;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryBySQL(Class<T> c, ResultSet rs, String sql, Object[] param)
    {
        String cacheId = SessionContext.getAutoCache();
        return this.queryCacheBySQL(c, cacheId, rs, sql, param);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T queryCacheBySQL(Class<T> c, String cacheId, ResultSet rs, String sql, Object... param)
    {
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && cacheId != null && ctx != null)
        {
            T ret = ctx.getSingle(c, cacheId, sql, param, false);
            if (ret != null)
                return ret;
        }
        // ---------
        T obj = super.queryBySQL(c, rs, sql);
        // ----------
        if (SessionContext.isUseCache() && ctx != null && cacheId != null)
        {
            ctx.cacheSingle(c, cacheId, sql, param, obj, false);
        }
        return obj;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> Page<T> queryPageListA(Class<T> c, String sql, int page, int pageSize, Object[] param)
    {
        String cacheId = SessionContext.getAutoCache();
        return this.queryCachePageList(c, cacheId, sql, page, pageSize, param);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> Page<T> queryCachePageList(Class<T> c, String cacheId, String sql, int page, int pageSize, Object... param)
    {
        CacheContext ctx = SessionContext.getCacheContext();
        if (SessionContext.isUseCache() && cacheId != null && ctx != null)
        {
            Page<T> ret = ctx.getPage(c, cacheId, sql, param, page, pageSize, false);
            if (ret != null)
                return ret;
        }
        // ---------
        Page<T> obj = super.queryPageList(c, sql, page, pageSize, param);
        // ----------
        if (SessionContext.isUseCache() && ctx != null && cacheId != null)
        {
            ctx.cachePage(c, cacheId, sql, param, page, pageSize, obj.getCount(), obj.getList(), false);
        }
        return obj;
    }
    
    /**
     * {@inheritDoc}
     */
    public <T> T getSqlMap(Class<T> c)
    {
        return ProxyBeanFactory.getSqlMapProxy(c, this);
    }
    
    /**
     * {@inheritDoc}
     */
    public SqlMapFactory getSqlMapFactory()
    {
        return sqlMapFactory;
    }
    
}