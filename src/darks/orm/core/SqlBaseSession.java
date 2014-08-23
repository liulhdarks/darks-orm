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

package darks.orm.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import darks.orm.exceptions.SessionException;

public abstract interface SqlBaseSession
{
    
    /**
     * Obtain database JDBC connection
     * 
     * @return Connection Instance
     */
    public Connection getConnection();
    
    /**
     * Obtain database JDBC connection
     * 
     * @param isCreate Whether to create the connection directly
     * @return Connection Instance
     */
    public Connection getConnection(boolean isCreate);
    
    public void setConnection(Connection conn)
        throws SessionException;
    
    /**
     * Close sql session
     */
    public void close();
    
    /**
     * Destroy the session
     */
    public void shutDown();
    
    /**
     * Whether already to close the session
     * 
     * @return true/false
     */
    public boolean isClosed();
    
    /**
     * Whether already to initialize session
     * 
     * @return true/false
     */
    public boolean isInited();
    
    /**
     * Whether the automatic commit transaction
     * 
     * @return true/false
     */
    public boolean isAutoCommit();
    
    public void setAutoCommit(boolean isAutoCommit)
        throws SQLException;
    
    /**
     * Commit transaction
     * 
     * @throws SQLException Sql excetpion
     */
    public void commit()
        throws SQLException;
    
    /**
     * Rollback transaction
     * 
     * @throws SQLException Sql excetpion
     */
    public void rollback()
        throws SQLException;
    
    /**
     * Rollback transaction to savepoint
     * 
     * @param point Savepoint object
     * @throws SQLException Sql excetpion
     */
    public void rollback(Savepoint point)
        throws SQLException;
    
    /**
     * Set save point
     * 
     * @return Savepoint object
     * @throws SQLException Sql excetpion
     */
    public Savepoint setSavepoint()
        throws SQLException;
    
    /**
     * Set save point by name
     * 
     * @param name save point name
     * @return Savepoint object
     * @throws SQLException Sql excetpion
     */
    public Savepoint setSavepoint(String name)
        throws SQLException;
    
}