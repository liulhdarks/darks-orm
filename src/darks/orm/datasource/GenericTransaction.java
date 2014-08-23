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

package darks.orm.datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

import darks.orm.core.config.DataSourceConfiguration;
import darks.orm.datasource.factory.ConnectionFactory;
import darks.orm.datasource.factory.StatementFactory.StatementType;
import darks.orm.exceptions.DataSourceException;
import darks.orm.exceptions.SessionException;
import darks.orm.util.JdbcHelper;

/**
 * 
 * 
 * <p>
 * <h1>GenericTransaction.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public class GenericTransaction implements Transaction
{
    
    private Connection conn;
    
    private DataSourceConfiguration dsc;
    
    private boolean autoCommit = true;
    
    public GenericTransaction()
    {
        
    }
    
    private void initialize()
    {
        try
        {
            if (conn == null || conn.isClosed())
            {
                conn = ConnectionFactory.getInstance().getConnection();
            }
        }
        catch (SQLException e)
        {
            throw new DataSourceException("GenericTransaction::initialize error " + e.toString(), e);
        }
        if (dsc == null)
        {
            dsc = ConnectionFactory.getInstance().getCurrentDataSourceConfig();
            autoCommit = dsc.isAutoCommit();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    {
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                throw new SessionException("GenericTransaction::close occur error", e);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void commit()
        throws SQLException
    {
        if (conn != null)
        {
            conn.commit();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection()
    {
        return getConnection(false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection(boolean isCreate)
    {
        if (isCreate)
        {
            initialize();
        }
        return conn;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement getPreparedStatement(String sql)
    {
        initialize();
        return getPreparedStatement(sql, dsc.getResultSetConfig().getStatementType());
    }
    
    /**
     * 
     * @return
     */
    public PreparedStatement getPreparedStatement(String sql, StatementType type)
    {
        return getPreparedStatement(sql, type, false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public PreparedStatement getPreparedStatement(String sql, StatementType type, boolean isDirect)
    {
        initialize();
        try
        {
            ProxyConnection proxy = (ProxyConnection)conn;
            if (isDirect)
            {
                return proxy.prepareStatement(sql);
            }
            return proxy.getStatementFactory().getPrepareStatement(sql, proxy, type);
        }
        catch (SQLException e)
        {
            throw new SessionException("GenericTransaction::getPreparedStatement occur error", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CallableStatement getCallableStatement(String sql)
    {
        initialize();
        ProxyConnection proxy = (ProxyConnection)conn;
        try
        {
            return proxy.prepareCall(sql);
        }
        catch (SQLException e)
        {
            throw new SessionException("GenericTransaction::getCallableStatement occur error", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAutoCommit()
    {
        initialize();
        return autoCommit;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isClosed()
    {
        if (conn == null)
            return true;
        try
        {
            return conn.isClosed();
        }
        catch (SQLException e)
        {
            throw new SessionException("GenericTransaction::isClosed occur error", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback()
        throws SQLException
    {
        initialize();
        conn.rollback();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback(Savepoint point)
        throws SQLException
    {
        initialize();
        conn.rollback(point);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setAutoCommit(boolean isAutoCommit)
        throws SQLException
    {
        initialize();
        autoCommit = isAutoCommit;
        conn.setAutoCommit(isAutoCommit);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnection(Connection connection)
    {
        if (!JdbcHelper.isConnectionClosed(conn))
        {
            close();
        }
        conn = connection;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Savepoint setSavepoint()
        throws SQLException
    {
        initialize();
        return conn.setSavepoint();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Savepoint setSavepoint(String name)
        throws SQLException
    {
        initialize();
        return conn.setSavepoint(name);
    }
    
}