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

package darks.orm.util;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;

import darks.orm.datasource.Transaction;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

/**
 * A tool class for JDBC operation
 * 
 * <p>
 * <h1>JdbcHelper.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public final class JdbcHelper
{
    
    private final static Logger logger = LoggerFactory.getLogger(JdbcHelper.class);
    
    /**
     * Default construction
     */
    private JdbcHelper()
    {
        
    }
    
    /**
     * Close Jdbc ResultSet object
     * 
     * @param rs ResultSet object
     */
    public static void closeResultSet(ResultSet rs)
    {
        if (rs != null)
        {
            try
            {
                rs.close();
            }
            catch (SQLException e)
            {
                logger.error("JdbcHelper::closeResultSet " + e.toString(), e);
            }
        }
    }
    
    /**
     * Close Jdbc Statement object
     * 
     * @param stmt Statement object
     */
    public static void closeStatement(Statement stmt)
    {
        if (stmt != null)
        {
            try
            {
                stmt.close();
            }
            catch (SQLException e)
            {
                logger.error("JdbcHelper::closeStatement " + e.toString(), e);
            }
        }
    }
    
    /**
     * Close Jdbc Connection object
     * 
     * @param conn JDBC Connection object
     */
    public static void closeConnection(Connection conn)
    {
        if (conn != null)
        {
            try
            {
                conn.close();
            }
            catch (SQLException e)
            {
                logger.error("JdbcHelper::closeConnection " + e.toString(), e);
            }
        }
    }
    
    /**
     * rollback transaction
     * 
     * @param tx Transaction object
     */
    public static void rollback(Transaction tx)
    {
        if (tx != null)
        {
            try
            {
                tx.rollback();
            }
            catch (SQLException e)
            {
                logger.error("JdbcHelper::rollback " + e.toString(), e);
            }
        }
    }
    
    /**
     * rollback transaction
     * 
     * @param tx Transaction object
     * @param point Savepoint object
     */
    public static void rollback(Transaction tx, Savepoint point)
    {
        if (tx != null)
        {
            try
            {
                tx.rollback(point);
            }
            catch (SQLException e)
            {
                logger.error("JdbcHelper::rollback " + e.toString(), e);
            }
        }
    }
    
    /**
     * Check JDBC connection is closed
     * 
     * @param conn JDBC connection
     */
    public static boolean isConnectionClosed(Connection conn)
    {
        boolean isClosed = false;
        if (conn == null)
        {
            isClosed = true;
        }
        else
        {
            try
            {
                if (conn.isClosed())
                {
                    isClosed = true;
                }
            }
            catch (SQLException e)
            {
                try
                {
                    conn.setAutoCommit(false);
                }
                catch (SQLException err)
                {
                    isClosed = true;
                }
            }
        }
        return isClosed;
    }
    
    /**
     * {@link Statement} set object parameter
     * 
     * @param index Parameter index
     * @param param Parameter value
     * @throws SQLException
     */
    public static void setObject(PreparedStatement pstmt, int index, Object param)
        throws SQLException
    {
        if (param instanceof Clob)
        {
            Clob clob = (Clob)param;
            pstmt.setCharacterStream(index, clob.getCharacterStream());
        }
        else if (param instanceof Blob)
        {
            Blob blob = (Blob)param;
            pstmt.setBinaryStream(index, blob.getBinaryStream(), blob.length());
        }
        else if (param instanceof InputStream)
        {
            InputStream ins = (InputStream)param;
            pstmt.setBinaryStream(index, ins);
        }
        else if (param instanceof java.util.Date)
        {
            java.util.Date date = (java.util.Date)param;
            Date dt = DateHelper.utilDateToSqlDate(date);
            pstmt.setObject(index, dt);
        }
        else
        {
            pstmt.setObject(index, param);
        }
    }
}