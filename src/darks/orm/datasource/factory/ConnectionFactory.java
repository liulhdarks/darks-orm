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

package darks.orm.datasource.factory;

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import darks.orm.core.config.Configuration;
import darks.orm.core.config.DataSourceConfiguration;
import darks.orm.core.config.ResultSetConfig;
import darks.orm.core.session.SessionContext;
import darks.orm.datasource.ConnectionHandler;
import darks.orm.datasource.ProxyConnection;
import darks.orm.util.JdbcHelper;
import darks.orm.util.ReflectHelper;

public class ConnectionFactory
{
    
    private volatile ConnectionHandler factory = null;
    
    private static volatile ConnectionFactory instance = null;
    
    private static ThreadLocal<Connection> connThreadLocal = new ThreadLocal<Connection>();
    
    private static ConcurrentMap<String, Class<? extends ConnectionHandler>> handlers =
        new ConcurrentHashMap<String, Class<? extends ConnectionHandler>>(3);
    
    private volatile DataSourceConfiguration currentDataSourceConfig = null;;
    static
    {
        handlers.put("jdbc", JdbcConnectionFactory.class);
        handlers.put("bonecp", BoneCPConnectionFactory.class);
        handlers.put("jndi", JndiConnectionFactory.class);
    }
    
    private ConnectionFactory()
    {
        if (SessionContext.getConfigure() == null)
        {
            SessionContext.build();
        }
        initFactory();
    }
    
    public void initFactory()
    {
        SessionContext.build();
        Configuration cfg = SessionContext.getConfigure();
        DataSourceConfiguration dsc = cfg.getMainDataSourceConfig();
        currentDataSourceConfig = dsc;
        Class<? extends ConnectionHandler> clazz = handlers.get(dsc.getType());
        factory = ReflectHelper.newInstance(clazz);
        factory.setDataSourceConfig(dsc);
        ConnectionHandler handler = factory;
        ConnectionHandler newhandler = null;
        while (dsc.getNext() != null)
        {
            dsc = dsc.getNext();
            clazz = handlers.get(dsc.getType());
            newhandler = ReflectHelper.newInstance(clazz);
            newhandler.setDataSourceConfig(dsc);
            handler.setHandler(newhandler);
            handler = newhandler;
        }
    }
    
    public void addConnectionFactory(String type, Class<? extends ConnectionHandler> clazz)
    {
        handlers.put(type, clazz);
    }
    
    /**
     * 单台模式，获取实例
     * 
     * @return
     */
    public static ConnectionFactory getInstance()
    {
        if (instance == null)
        {
            instance = new ConnectionFactory();
        }
        return instance;
    }
    
    /**
     * 获得连接
     * 
     * @return 连接
     * @throws Exception
     */
    public Connection getConnection()
    {
        return getConnection(SessionContext.getConfigure().isUseThreadLocal());
    }
    
    /**
     * 获得连接
     * 
     * @param type 连接类型 0:JDBC 1:BoneCP 2:JNDI
     * @return
     */
    public Connection getConnection(boolean withThreadLocal)
    {
        if (!withThreadLocal)
        {
            if (DataSourceFactory.getInstance().isVaild())
            {
                return DataSourceFactory.getInstance().getConnection();
            }
            Connection conn = factory.getConnection();
            if (conn == null)
                return null;
            return new ProxyConnection(conn);
        }
        
        Connection con = connThreadLocal.get();
        
        boolean isClosed = JdbcHelper.isConnectionClosed(con);
        if (isClosed)
        {
            con = null;
            if (DataSourceFactory.getInstance().isVaild())
            {
                con = DataSourceFactory.getInstance().getConnection();
            }
            else
            {
                con = factory.getConnection();
            }
            ProxyConnection proxy = new ProxyConnection(con);
            con = proxy;
            connThreadLocal.set(con);
        }
        
        return con;
    }
    
    public void closeCurrentConnection()
        throws Exception
    {
        if (SessionContext.getConfigure().isUseThreadLocal())
        {
            Connection con = connThreadLocal.get();
            if (con != null)
            {
                con.close();
            }
            connThreadLocal.set(null);
        }
    }
    
    public void setFactory(ConnectionHandler factory)
    {
        this.factory = factory;
    }
    
    public void setCurrentDataSourceConfig(DataSourceConfiguration currentDataSourceConfig)
    {
        this.currentDataSourceConfig = currentDataSourceConfig;
    }
    
    public DataSourceConfiguration getCurrentDataSourceConfig()
    {
        return currentDataSourceConfig;
    }
    
    public ResultSetConfig getCurrentResultSetConfig()
    {
        return getCurrentDataSourceConfig().getResultSetConfig();
    }
    
}