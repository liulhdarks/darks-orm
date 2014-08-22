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

package darks.orm.datasource;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import darks.orm.core.config.DataSourceConfiguration;
import darks.orm.core.config.ResultSetConfig;
import darks.orm.datasource.factory.ConnectionFactory;
import darks.orm.datasource.factory.StatementFactory;
import darks.orm.datasource.factory.StatementFactory.StatementType;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.JdbcHelper;

/**
 * JDBC database connection proxy class
 * 
 * <p>
 * <h1>ProxyConnection.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 * @see java.sql.Connection
 */
public class ProxyConnection implements Connection
{
    
    private static Logger logger = LoggerFactory.getLogger(ProxyConnection.class);
    
    /**
     * Jdbc connnection object
     */
    private Connection conn;
    
    /**
     * Note database connection is closed
     */
    private volatile boolean closed = false;
    
    private StatementFactory stmtFactory = new StatementFactory();
    
    private DataSourceConfiguration dsConfig;
    
    public ProxyConnection(Connection conn)
    {
        this.conn = conn;
        dsConfig = ConnectionFactory.getInstance().getCurrentDataSourceConfig();
    }
    
    /**
     * Get local connection object
     * 
     * @return Jdbc connection object
     */
    private Connection getConnection()
    {
        return conn;
    }
    
    public StatementType getStatementType()
    {
        return dsConfig.getResultSetConfig().getStatementType();
    }
    
    /**
     * Remove prepared statement object by sql key.
     * 
     * @param sql SQL sentence
     */
    public void removePreparedStatement(String sql)
    {
        stmtFactory.removePreparedStatement(sql);
    }
    
    /**
     * Remove callable statement object by sql key.
     * 
     * @param sql SQL sentence
     */
    public void removeCallableStatement(String sql)
    {
        stmtFactory.removeCallableStatement(sql);
    }
    
    @Override
    public void clearWarnings()
        throws SQLException
    {
        getConnection().clearWarnings();
    }
    
    @Override
    public void close()
        throws SQLException
    {
        closed = true;
        stmtFactory.close();
        if (conn != null)
        {
            logger.debug("Connection Closed");
            conn.close();
            conn = null;
        }
    }
    
    @Override
    public void commit()
        throws SQLException
    {
        getConnection().commit();
    }
    
    @Override
    public Array createArrayOf(String typeName, Object[] elements)
        throws SQLException
    {
        return getConnection().createArrayOf(typeName, elements);
    }
    
    @Override
    public Blob createBlob()
        throws SQLException
    {
        return getConnection().createBlob();
    }
    
    @Override
    public Clob createClob()
        throws SQLException
    {
        return getConnection().createClob();
    }
    
    @Override
    public NClob createNClob()
        throws SQLException
    {
        return getConnection().createNClob();
    }
    
    @Override
    public SQLXML createSQLXML()
        throws SQLException
    {
        return getConnection().createSQLXML();
    }
    
    @Override
    public Statement createStatement()
        throws SQLException
    {
        return getConnection().createStatement();
    }
    
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
        throws SQLException
    {
        return getConnection().createStatement(resultSetType, resultSetConcurrency);
    }
    
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        throws SQLException
    {
        return getConnection().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public Struct createStruct(String typeName, Object[] attributes)
        throws SQLException
    {
        return getConnection().createStruct(typeName, attributes);
    }
    
    @Override
    public boolean getAutoCommit()
        throws SQLException
    {
        return getConnection().getAutoCommit();
    }
    
    @Override
    public String getCatalog()
        throws SQLException
    {
        return getConnection().getCatalog();
    }
    
    @Override
    public Properties getClientInfo()
        throws SQLException
    {
        return getConnection().getClientInfo();
    }
    
    @Override
    public String getClientInfo(String name)
        throws SQLException
    {
        return getConnection().getClientInfo(name);
    }
    
    @Override
    public int getHoldability()
        throws SQLException
    {
        return getConnection().getHoldability();
    }
    
    @Override
    public DatabaseMetaData getMetaData()
        throws SQLException
    {
        return getConnection().getMetaData();
    }
    
    @Override
    public int getTransactionIsolation()
        throws SQLException
    {
        return getConnection().getTransactionIsolation();
    }
    
    @Override
    public Map<String, Class<?>> getTypeMap()
        throws SQLException
    {
        return getConnection().getTypeMap();
    }
    
    @Override
    public SQLWarning getWarnings()
        throws SQLException
    {
        return getConnection().getWarnings();
    }
    
    @Override
    public boolean isClosed()
        throws SQLException
    {
        if (!closed)
        {
            if (JdbcHelper.isConnectionClosed(getConnection()))
            {
                closed = true;
            }
        }
        return closed;
    }
    
    @Override
    public boolean isReadOnly()
        throws SQLException
    {
        return getConnection().isReadOnly();
    }
    
    @Override
    public boolean isValid(int timeout)
        throws SQLException
    {
        return getConnection().isValid(timeout);
    }
    
    @Override
    public String nativeSQL(String sql)
        throws SQLException
    {
        return getConnection().nativeSQL(sql);
    }
    
    @Override
    public CallableStatement prepareCall(String sql)
        throws SQLException
    {
        return stmtFactory.getCallableStatement(this, sql);
    }
    
    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
        throws SQLException
    {
        ResultSetConfig rsc = dsConfig.getResultSetConfig();
        return getConnection().prepareCall(sql, rsc.getType(), rsc.getConcurrency());
    }
    
    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability)
        throws SQLException
    {
        return getConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public PreparedStatement prepareStatement(String sql)
        throws SQLException
    {
        return getConnection().prepareStatement(sql);
    }
    
    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
        throws SQLException
    {
        return getConnection().prepareStatement(sql, autoGeneratedKeys);
    }
    
    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
        throws SQLException
    {
        return getConnection().prepareStatement(sql, columnIndexes);
    }
    
    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
        throws SQLException
    {
        return getConnection().prepareStatement(sql, columnNames);
    }
    
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
        throws SQLException
    {
        ResultSetConfig rsc = dsConfig.getResultSetConfig();
        return getConnection().prepareStatement(sql, rsc.getType(), rsc.getConcurrency());
    }
    
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability)
        throws SQLException
    {
        return getConnection().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }
    
    @Override
    public void releaseSavepoint(Savepoint savepoint)
        throws SQLException
    {
        getConnection().releaseSavepoint(savepoint);
    }
    
    @Override
    public void rollback()
        throws SQLException
    {
        getConnection().rollback();
    }
    
    @Override
    public void rollback(Savepoint savepoint)
        throws SQLException
    {
        getConnection().rollback(savepoint);
    }
    
    @Override
    public void setAutoCommit(boolean autoCommit)
        throws SQLException
    {
        getConnection().setAutoCommit(autoCommit);
    }
    
    @Override
    public void setCatalog(String catalog)
        throws SQLException
    {
        getConnection().setCatalog(catalog);
    }
    
    @Override
    public void setClientInfo(Properties properties)
        throws SQLClientInfoException
    {
        getConnection().setClientInfo(properties);
    }
    
    @Override
    public void setClientInfo(String name, String value)
        throws SQLClientInfoException
    {
        getConnection().setClientInfo(name, value);
    }
    
    @Override
    public void setHoldability(int holdability)
        throws SQLException
    {
        getConnection().setHoldability(holdability);
    }
    
    @Override
    public void setReadOnly(boolean readOnly)
        throws SQLException
    {
        getConnection().setReadOnly(readOnly);
    }
    
    @Override
    public Savepoint setSavepoint()
        throws SQLException
    {
        return getConnection().setSavepoint();
    }
    
    @Override
    public Savepoint setSavepoint(String name)
        throws SQLException
    {
        return getConnection().setSavepoint(name);
    }
    
    @Override
    public void setTransactionIsolation(int level)
        throws SQLException
    {
        getConnection().setTransactionIsolation(level);
    }
    
    @Override
    public void setTypeMap(Map<String, Class<?>> map)
        throws SQLException
    {
        getConnection().setTypeMap(map);
    }
    
    @Override
    public boolean isWrapperFor(Class<?> iface)
        throws SQLException
    {
        return getConnection().isWrapperFor(iface);
    }
    
    @Override
    public <T> T unwrap(Class<T> iface)
        throws SQLException
    {
        return getConnection().unwrap(iface);
    }
    
    public StatementFactory getStatementFactory()
    {
        return stmtFactory;
    }
    
}
