
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
