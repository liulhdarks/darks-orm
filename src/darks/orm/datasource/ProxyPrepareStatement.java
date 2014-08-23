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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import darks.orm.util.JdbcHelper;

public class ProxyPrepareStatement implements PreparedStatement
{
    
    private PreparedStatement pstmt;
    
    private ProxyConnection conn;
    
    private volatile boolean isClosed = false;
    
    private String sql;
    
    public ProxyPrepareStatement()
    {
        
    }
    
    public ProxyPrepareStatement(String sql, ProxyConnection conn, PreparedStatement pstmt)
    {
        this.sql = sql;
        this.pstmt = pstmt;
        this.conn = conn;
        isClosed = false;
    }
    
    @Override
    public void addBatch()
        throws SQLException
    {
        pstmt.addBatch();
    }
    
    @Override
    public void clearParameters()
        throws SQLException
    {
        pstmt.clearParameters();
    }
    
    @Override
    public boolean execute()
        throws SQLException
    {
        return pstmt.execute();
    }
    
    @Override
    public ResultSet executeQuery()
        throws SQLException
    {
        return pstmt.executeQuery();
    }
    
    @Override
    public int executeUpdate()
        throws SQLException
    {
        return pstmt.executeUpdate();
    }
    
    @Override
    public ResultSetMetaData getMetaData()
        throws SQLException
    {
        return pstmt.getMetaData();
    }
    
    @Override
    public ParameterMetaData getParameterMetaData()
        throws SQLException
    {
        return pstmt.getParameterMetaData();
    }
    
    @Override
    public void setArray(int parameterIndex, Array x)
        throws SQLException
    {
        pstmt.setArray(parameterIndex, x);
    }
    
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x)
        throws SQLException
    {
        pstmt.setAsciiStream(parameterIndex, x);
    }
    
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length)
        throws SQLException
    {
        pstmt.setAsciiStream(parameterIndex, x, length);
    }
    
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length)
        throws SQLException
    {
        pstmt.setAsciiStream(parameterIndex, x, length);
    }
    
    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x)
        throws SQLException
    {
        pstmt.setBigDecimal(parameterIndex, x);
    }
    
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x)
        throws SQLException
    {
        pstmt.setBinaryStream(parameterIndex, x);
    }
    
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length)
        throws SQLException
    {
        pstmt.setBinaryStream(parameterIndex, x, length);
    }
    
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length)
        throws SQLException
    {
        pstmt.setBinaryStream(parameterIndex, x, length);
    }
    
    @Override
    public void setBlob(int parameterIndex, Blob x)
        throws SQLException
    {
        pstmt.setBlob(parameterIndex, x);
    }
    
    @Override
    public void setBlob(int parameterIndex, InputStream inputStream)
        throws SQLException
    {
        pstmt.setBlob(parameterIndex, inputStream);
    }
    
    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length)
        throws SQLException
    {
        pstmt.setBlob(parameterIndex, inputStream, length);
    }
    
    @Override
    public void setBoolean(int parameterIndex, boolean x)
        throws SQLException
    {
        pstmt.setBoolean(parameterIndex, x);
    }
    
    @Override
    public void setByte(int parameterIndex, byte x)
        throws SQLException
    {
        pstmt.setByte(parameterIndex, x);
    }
    
    @Override
    public void setBytes(int parameterIndex, byte[] x)
        throws SQLException
    {
        pstmt.setBytes(parameterIndex, x);
    }
    
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader)
        throws SQLException
    {
        pstmt.setCharacterStream(parameterIndex, reader);
    }
    
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length)
        throws SQLException
    {
        pstmt.setCharacterStream(parameterIndex, reader, length);
    }
    
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length)
        throws SQLException
    {
        pstmt.setCharacterStream(parameterIndex, reader, length);
    }
    
    @Override
    public void setClob(int parameterIndex, Clob x)
        throws SQLException
    {
        pstmt.setClob(parameterIndex, x);
    }
    
    @Override
    public void setClob(int parameterIndex, Reader reader)
        throws SQLException
    {
        pstmt.setClob(parameterIndex, reader);
    }
    
    @Override
    public void setClob(int parameterIndex, Reader reader, long length)
        throws SQLException
    {
        pstmt.setClob(parameterIndex, reader, length);
    }
    
    @Override
    public void setDate(int parameterIndex, Date x)
        throws SQLException
    {
        pstmt.setDate(parameterIndex, x);
    }
    
    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal)
        throws SQLException
    {
        pstmt.setDate(parameterIndex, x, cal);
    }
    
    @Override
    public void setDouble(int parameterIndex, double x)
        throws SQLException
    {
        pstmt.setDouble(parameterIndex, x);
    }
    
    @Override
    public void setFloat(int parameterIndex, float x)
        throws SQLException
    {
        pstmt.setFloat(parameterIndex, x);
    }
    
    @Override
    public void setInt(int parameterIndex, int x)
        throws SQLException
    {
        pstmt.setInt(parameterIndex, x);
    }
    
    @Override
    public void setLong(int parameterIndex, long x)
        throws SQLException
    {
        pstmt.setLong(parameterIndex, x);
    }
    
    @Override
    public void setNCharacterStream(int parameterIndex, Reader value)
        throws SQLException
    {
        pstmt.setNCharacterStream(parameterIndex, value);
    }
    
    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length)
        throws SQLException
    {
        pstmt.setNCharacterStream(parameterIndex, value, length);
    }
    
    @Override
    public void setNClob(int parameterIndex, NClob value)
        throws SQLException
    {
        pstmt.setNClob(parameterIndex, value);
    }
    
    @Override
    public void setNClob(int parameterIndex, Reader reader)
        throws SQLException
    {
        pstmt.setNClob(parameterIndex, reader);
    }
    
    @Override
    public void setNClob(int parameterIndex, Reader reader, long length)
        throws SQLException
    {
        pstmt.setNClob(parameterIndex, reader);
    }
    
    @Override
    public void setNString(int parameterIndex, String value)
        throws SQLException
    {
        pstmt.setNString(parameterIndex, value);
    }
    
    @Override
    public void setNull(int parameterIndex, int sqlType)
        throws SQLException
    {
        pstmt.setNull(parameterIndex, sqlType);
    }
    
    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName)
        throws SQLException
    {
        pstmt.setNull(parameterIndex, sqlType, typeName);
    }
    
    @Override
    public void setObject(int parameterIndex, Object x)
        throws SQLException
    {
        pstmt.setObject(parameterIndex, x);
    }
    
    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType)
        throws SQLException
    {
        pstmt.setObject(parameterIndex, x, targetSqlType);
    }
    
    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength)
        throws SQLException
    {
        pstmt.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }
    
    @Override
    public void setRef(int parameterIndex, Ref x)
        throws SQLException
    {
        pstmt.setRef(parameterIndex, x);
    }
    
    @Override
    public void setRowId(int parameterIndex, RowId x)
        throws SQLException
    {
        pstmt.setRowId(parameterIndex, x);
    }
    
    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject)
        throws SQLException
    {
        pstmt.setSQLXML(parameterIndex, xmlObject);
    }
    
    @Override
    public void setShort(int parameterIndex, short x)
        throws SQLException
    {
        pstmt.setShort(parameterIndex, x);
    }
    
    @Override
    public void setString(int parameterIndex, String x)
        throws SQLException
    {
        pstmt.setString(parameterIndex, x);
    }
    
    @Override
    public void setTime(int parameterIndex, Time x)
        throws SQLException
    {
        pstmt.setTime(parameterIndex, x);
    }
    
    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal)
        throws SQLException
    {
        pstmt.setTime(parameterIndex, x, cal);
    }
    
    @Override
    public void setTimestamp(int parameterIndex, Timestamp x)
        throws SQLException
    {
        pstmt.setTimestamp(parameterIndex, x);
    }
    
    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
        throws SQLException
    {
        pstmt.setTimestamp(parameterIndex, x, cal);
    }
    
    @Override
    public void setURL(int parameterIndex, URL x)
        throws SQLException
    {
        pstmt.setURL(parameterIndex, x);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length)
        throws SQLException
    {
        pstmt.setUnicodeStream(parameterIndex, x, length);
    }
    
    @Override
    public void addBatch(String sql)
        throws SQLException
    {
        pstmt.addBatch(sql);
    }
    
    @Override
    public void cancel()
        throws SQLException
    {
        pstmt.cancel();
    }
    
    @Override
    public void clearBatch()
        throws SQLException
    {
        pstmt.clearBatch();
    }
    
    @Override
    public void clearWarnings()
        throws SQLException
    {
        pstmt.clearWarnings();
    }
    
    @Override
    public void close()
        throws SQLException
    {
        conn.removePreparedStatement(sql);
        pstmt.close();
        isClosed = true;
    }
    
    @Override
    public boolean execute(String sql)
        throws SQLException
    {
        return pstmt.execute(sql);
    }
    
    @Override
    public boolean execute(String sql, int autoGeneratedKeys)
        throws SQLException
    {
        return pstmt.execute(sql, autoGeneratedKeys);
    }
    
    @Override
    public boolean execute(String sql, int[] columnIndexes)
        throws SQLException
    {
        return pstmt.execute(sql, columnIndexes);
    }
    
    @Override
    public boolean execute(String sql, String[] columnNames)
        throws SQLException
    {
        return pstmt.execute(sql, columnNames);
    }
    
    @Override
    public int[] executeBatch()
        throws SQLException
    {
        return pstmt.executeBatch();
    }
    
    @Override
    public ResultSet executeQuery(String sql)
        throws SQLException
    {
        return pstmt.executeQuery(sql);
    }
    
    @Override
    public int executeUpdate(String sql)
        throws SQLException
    {
        return pstmt.executeUpdate(sql);
    }
    
    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys)
        throws SQLException
    {
        return pstmt.executeUpdate(sql, autoGeneratedKeys);
    }
    
    @Override
    public int executeUpdate(String sql, int[] columnIndexes)
        throws SQLException
    {
        return pstmt.executeUpdate(sql, columnIndexes);
    }
    
    @Override
    public int executeUpdate(String sql, String[] columnNames)
        throws SQLException
    {
        return pstmt.executeUpdate(sql, columnNames);
    }
    
    @Override
    public Connection getConnection()
        throws SQLException
    {
        return conn;
    }
    
    @Override
    public int getFetchDirection()
        throws SQLException
    {
        return pstmt.getFetchDirection();
    }
    
    @Override
    public int getFetchSize()
        throws SQLException
    {
        return pstmt.getFetchSize();
    }
    
    @Override
    public ResultSet getGeneratedKeys()
        throws SQLException
    {
        return pstmt.getGeneratedKeys();
    }
    
    @Override
    public int getMaxFieldSize()
        throws SQLException
    {
        return pstmt.getMaxFieldSize();
    }
    
    @Override
    public int getMaxRows()
        throws SQLException
    {
        return pstmt.getMaxRows();
    }
    
    @Override
    public boolean getMoreResults()
        throws SQLException
    {
        return pstmt.getMoreResults();
    }
    
    @Override
    public boolean getMoreResults(int current)
        throws SQLException
    {
        return pstmt.getMoreResults(current);
    }
    
    @Override
    public int getQueryTimeout()
        throws SQLException
    {
        return pstmt.getQueryTimeout();
    }
    
    @Override
    public ResultSet getResultSet()
        throws SQLException
    {
        return pstmt.getResultSet();
    }
    
    @Override
    public int getResultSetConcurrency()
        throws SQLException
    {
        return pstmt.getResultSetConcurrency();
    }
    
    @Override
    public int getResultSetHoldability()
        throws SQLException
    {
        return pstmt.getResultSetHoldability();
    }
    
    @Override
    public int getResultSetType()
        throws SQLException
    {
        return pstmt.getResultSetType();
    }
    
    @Override
    public int getUpdateCount()
        throws SQLException
    {
        return pstmt.getUpdateCount();
    }
    
    @Override
    public SQLWarning getWarnings()
        throws SQLException
    {
        return pstmt.getWarnings();
    }
    
    @Override
    public boolean isClosed()
        throws SQLException
    {
        if (!isClosed)
        {
            if (JdbcHelper.isConnectionClosed(conn))
            {
                isClosed = true;
            }
        }
        return isClosed;
    }
    
    @Override
    public boolean isPoolable()
        throws SQLException
    {
        return pstmt.isPoolable();
    }
    
    @Override
    public void setCursorName(String name)
        throws SQLException
    {
        pstmt.setCursorName(name);
    }
    
    @Override
    public void setEscapeProcessing(boolean enable)
        throws SQLException
    {
        pstmt.setEscapeProcessing(enable);
    }
    
    @Override
    public void setFetchDirection(int direction)
        throws SQLException
    {
        pstmt.setFetchDirection(direction);
    }
    
    @Override
    public void setFetchSize(int rows)
        throws SQLException
    {
        pstmt.setFetchSize(rows);
    }
    
    @Override
    public void setMaxFieldSize(int max)
        throws SQLException
    {
        pstmt.setMaxFieldSize(max);
    }
    
    @Override
    public void setMaxRows(int max)
        throws SQLException
    {
        pstmt.setMaxRows(max);
    }
    
    @Override
    public void setPoolable(boolean poolable)
        throws SQLException
    {
        pstmt.setPoolable(poolable);
    }
    
    @Override
    public void setQueryTimeout(int seconds)
        throws SQLException
    {
        pstmt.setQueryTimeout(seconds);
    }
    
    @Override
    public boolean isWrapperFor(Class<?> iface)
        throws SQLException
    {
        return pstmt.isWrapperFor(iface);
    }
    
    @Override
    public <T> T unwrap(Class<T> iface)
        throws SQLException
    {
        return pstmt.unwrap(iface);
    }
    
}