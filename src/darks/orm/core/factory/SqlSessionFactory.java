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

package darks.orm.core.factory;

import darks.orm.app.SqlSession;
import darks.orm.core.SqlBeanSession;
import darks.orm.core.SqlJdbcSession;
import darks.orm.core.cache.CacheContext;
import darks.orm.core.cache.scope.CacheFactory;
import darks.orm.core.config.Configuration;
import darks.orm.core.session.SessionContext;
import darks.orm.core.session.SqlSessionImpl;

/**
 * Note that sql session object according
 * 
 * <p>
 * <h1>SqlSessionFactory.java</h1>
 * <p>
 * 
 * For example:
 * 
 * Using default configuration file path.
 * 
 * <pre>
 * SqlSession session = SqlSessionFactory.getSession();
 * </pre>
 * 
 * Using custom configuration file path.
 * 
 * <pre>
 * Configuration cfg = SessionConfigFactory.getConfiguration(&quot;/darks.xml&quot;);
 * SqlSessionFactory.initialize(cfg);
 * SqlSession session = SqlSessionFactory.getSession();
 * </pre>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.2
 * @see darks.orm.app.SqlSession
 * @see darks.orm.core.config.SessionConfigFactory
 * @see darks.orm.core.config.Configuration
 */
public final class SqlSessionFactory
{
    
    private static final ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();
    
    private SqlSessionFactory()
    {
        
    }
    
    /**
     * Initialize SqlSessionFactory configuration
     */
    private static void initialize()
    {
        if (SessionContext.getConfigure() == null || !SessionContext.isInited())
        {
            SessionContext.build();
        }
    }
    
    /**
     * Initialize SqlSessionFactory configuration
     * 
     * @param cfg Configuration object
     */
    public static void initialize(Configuration cfg)
    {
        if (SessionContext.getConfigure() == null || !SessionContext.isInited())
        {
            SessionContext.build(cfg);
        }
    }
    
    /**
     * Create and get sql session object
     * 
     * @return SqlSession Session object
     */
    public static SqlSession getSession()
    {
        initialize();
        if (!SessionContext.getConfigure().isUseThreadLocal())
        {
            return new SqlSessionImpl();
        }
        SqlSession session = threadLocal.get();
        if (session != null && !session.isClosed())
        {
            return session;
        }
        session = new SqlSessionImpl();
        threadLocal.set(session);
        return session;
    }
    
    /**
     * Create and get sql session object
     * 
     * @param isCreate Whether directly to create a new session
     * @return SqlSession Session object
     */
    public static SqlSession getSession(boolean isCreate)
    {
        if (isCreate)
        {
            return new SqlSessionImpl();
        }
        return getSession();
    }
    
    /**
     * Create and get sql jdbc session object, which just has jdbc operation API
     * function.
     * 
     * @return SqlJdbcSession JDBC session object
     */
    public static SqlJdbcSession getJdbcSession()
    {
        return getSession();
    }
    
    /**
     * Create and get sql entity bean session object, which just has entity bean
     * operation API function.
     * 
     * @return SqlBeanSession Entity bean session object
     */
    public static SqlBeanSession getBeanSession()
    {
        return getSession();
    }
    
    /**
     * Judging whether the current thread SQL session object have been closed.
     * 
     * @return
     */
    public static boolean isCurrentSessionClosed()
    {
        SqlSession session = threadLocal.get();
        if (session == null || session.isClosed())
        {
            return true;
        }
        return false;
        
    }
    
    /**
     * Close the current thread session.
     */
    public static void closeCurrentSession()
    {
        SqlSession session = threadLocal.get();
        if (session != null)
        {
            session.close();
            threadLocal.set(null);
        }
    }
    
    /**
     * Add CacheFactory extension object
     * 
     * @param key Key value
     * @param cacheFactory Cache factory object
     */
    public void addCacheFactory(String key, CacheFactory cacheFactory)
    {
        initialize();
        CacheContext ctx = SessionContext.getCacheContext();
        if (ctx != null)
        {
            ctx.addCacheFactory(key, cacheFactory);
        }
    }
}