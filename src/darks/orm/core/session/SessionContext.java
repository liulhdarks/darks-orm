/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package darks.orm.core.session;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import darks.orm.core.cache.CacheContext;
import darks.orm.core.config.CacheConfiguration;
import darks.orm.core.config.Configuration;
import darks.orm.core.config.DataSourceConfiguration;
import darks.orm.core.config.EntityConfiguration;
import darks.orm.core.config.SessionConfigFactory;
import darks.orm.core.config.CacheConfiguration.CacheConfigType;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.factory.SqlMapSingletonFactory;
import darks.orm.exceptions.SessionException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.LogHelper;
import darks.orm.util.ThreadHelper;

/**
 * 
 * 
 * <p>
 * <h1>SessionContext.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public class SessionContext
{
    
    private static final Logger logger = LoggerFactory.getLogger(SessionContext.class);
    
    private static Configuration configure = null;
    
    private static volatile boolean isInited = false;
    
    private static final Lock lock = new ReentrantLock();
    
    private static CacheContext cacheContext = null;
    
    /**
     * Build session context
     */
    public static void build()
    {
        lock.lock();
        try
        {
            if (configure == null || isInited == false)
            {
                isInited = true;
                Configuration config = SessionConfigFactory.getConfiguration();
                SessionContext.setConfigure(config);
                initialize(config);
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    /**
     * Build session context
     * 
     * @param cfgPath configuration file path
     */
    public static void build(String cfgPath)
    {
        lock.lock();
        try
        {
            if (configure == null || isInited == false)
            {
                isInited = true;
                Configuration config = SessionConfigFactory.getConfiguration(cfgPath);
                SessionContext.setConfigure(config);
                initialize(config);
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    /**
     * Build session context
     * 
     * @param config Configuration object
     */
    public static void build(Configuration config)
    {
        if (config == null)
            return;
        lock.lock();
        try
        {
            if (configure == null || isInited == false)
            {
                isInited = true;
                SessionContext.setConfigure(config);
                initialize(config);
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    /**
     * Global initialization
     * 
     * @param config Configuration object
     */
    private static void initialize(Configuration config)
    {
        EntityConfiguration ecfg = config.getEntityConfig();
        Collection<Class<?>> entitys = ecfg.getEntitys();
        try
        {
            ClassFactory.initEntity(entitys);
        }
        catch (ClassNotFoundException e)
        {
            LogHelper.except(logger, "ClassFactory parse entitys configuration error.", SessionException.class, e);
        }
        SqlMapSingletonFactory.getInstance().executeDDLMap();
        if (config.getCacheConfig().isUseCache())
        {
            cacheContext = new CacheContext();
            cacheContext.initialize();
        }
    }
    
    /**
     * Global destroyed
     */
    public static void destroy()
    {
        lock.lock();
        try
        {
            if (cacheContext != null)
                cacheContext.shutdown();
            try
            {
                Collection<DataSourceConfiguration> cols = getConfigure().getDataSourceConfigs();
                for (DataSourceConfiguration dsc : cols)
                {
                    dsc.destroy();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            configure = null;
            isInited = false;
            ThreadHelper.shutdownNow();
        }
        finally
        {
            lock.unlock();
        }
    }
    
    /**
     * Whether the cache configuration utility
     * 
     * @return true/false
     */
    public static boolean isUseCache()
    {
        if (cacheContext == null || configure == null)
            return false;
        return configure.getCacheConfig().isUseCache();
    }
    
    /**
     * Access automatic cache ID
     * 
     * @return Cache ID
     */
    public static String getAutoCache()
    {
        CacheConfiguration cacheConfig = configure.getCacheConfig();
        if (!isUseCache() || cacheConfig.getCacheConfigType() == CacheConfigType.Manual
            || cacheConfig.getAutoCacheId() == null || "".equals(cacheConfig.getAutoCacheId()))
            return null;
        return cacheConfig.getAutoCacheId();
    }
    
    public static Configuration getConfigure()
    {
        return configure;
    }
    
    public static void setConfigure(Configuration configure)
    {
        SessionContext.configure = configure;
    }
    
    public static boolean isInited()
    {
        return isInited;
    }
    
    public static CacheContext getCacheContext()
    {
        return cacheContext;
    }
    
}
