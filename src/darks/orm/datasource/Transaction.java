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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;

import darks.orm.datasource.factory.StatementFactory.StatementType;

/**
 * 
 * <p>
 * <h1>Transaction.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public interface Transaction
{
    
    /**
     * 
     * @return
     */
    public Connection getConnection();
    
    /**
     * 
     * @return
     */
    public Connection getConnection(boolean isCreate);
    
    /**
     * 
     * @param connection
     */
    public void setConnection(Connection connection);
    
    /**
     * 
     * @return
     */
    public PreparedStatement getPreparedStatement(String sql);
    
    /**
     * 
     * @return
     */
    public PreparedStatement getPreparedStatement(String sql, StatementType type);
    
    /**
     * 
     * @param isDirect
     * @return
     */
    public PreparedStatement getPreparedStatement(String sql, StatementType type, boolean isDirect);
    
    /**
     * 
     * @param sql
     * @return
     */
    public CallableStatement getCallableStatement(String sql);
    
    /**
     * 
     */
    public void close();
    
    /**
     * 
     * @return
     */
    public boolean isClosed();
    
    /**
     * 
     * @return
     */
    public boolean isAutoCommit();
    
    /**
     * 
     * @param isAutoCommit
     * @throws SQLException
     */
    public void setAutoCommit(boolean isAutoCommit)
        throws SQLException;
    
    /**
     * 
     * @throws SQLException
     */
    public void commit()
        throws SQLException;
    
    /**
     * 
     * @throws SQLException
     */
    public void rollback()
        throws SQLException;
    
    /**
     * 
     * @param point
     * @throws SQLException
     */
    public void rollback(Savepoint point)
        throws SQLException;
    
    /**
     * 
     * @return
     * @throws SQLException
     */
    public Savepoint setSavepoint()
        throws SQLException;
    
    /**
     * 
     * @param name
     * @return
     * @throws SQLException
     */
    public Savepoint setSavepoint(String name)
        throws SQLException;
}
