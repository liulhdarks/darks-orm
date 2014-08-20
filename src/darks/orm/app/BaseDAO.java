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

package darks.orm.app;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import darks.orm.app.factory.SqlMapFactory;
import darks.orm.core.factory.SqlSessionFactory;
import darks.orm.core.session.SessionContext;

/**
 * The base data access object, which help to wraps {@link SqlSession} object.
 * We just need to create a DAO class extended BaseDAO.
 * <p>
 * <h1>BaseDAO.java</h1>
 * <p>
 * For example:
 * 
 * <pre>
 *      public class UserDAO extends BaseDAO {
 *           ...
 *      }
 * </pre>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/01/2012
 * @since JDK1.5
 */
public class BaseDAO
{
    
    /**
     * Query SELF
     */
    public static final String SELF = "_SELF";
    
    private SqlSession session = null;
    
    /**
     * <Default constructor>
     */
    public BaseDAO()
    {
        if (!SessionContext.isInited())
        {
            SessionContext.build();
        }
    }
    
    /**
     * Obtain sql session object
     * 
     * @return sql session object
     */
    protected SqlSession getSession()
    {
        if (session != null && !session.isClosed())
        {
            return session;
        }
        session = SqlSessionFactory.getSession();
        return session;
    }
    
    /**
     * Obtain database JDBC connection
     * 
     * @return Connection Instance
     */
    public Connection getConnection()
    {
        return getSession().getConnection();
    }
    
    /**
     * Obtain database JDBC connection
     * 
     * @param isCreate Whether to create the connection directly
     * @return Connection Instance
     */
    public Connection getConnection(boolean isCreate)
    {
        return getSession().getConnection(isCreate);
    }
    
    /**
     * <Set custom connection>
     * @param conn JDBC connection
     */
    public void setConnection(Connection conn)
    {
        getSession().setConnection(conn);
    }
    
    /**
     * Close sql session
     */
    public void close()
    {
        getSession().close();
    }
    
    /**
     * Whether already to close the session
     * 
     * @return true/false
     */
    public boolean isClosed()
    {
        return getSession().isClosed();
    }
    
    /**
     * Destroy the session
     */
    public void shutDown()
    {
        getSession().shutDown();
    }
    
    /**
     * Close session object in current thread
     * 
     */
    public static void closeCurrentSession()
    {
        SqlSessionFactory.closeCurrentSession();
    }
    
    /**
     * Query dataset fields
     * 
     * @param sql SQL statement
     * @param params Paraneters
     * @return result
     */
    public int executeField(String sql, Object... params)
    {
        return getSession().executeField(sql, params);
    }
    
    /**
     * Execute query operation
     * 
     * @param sql SQL statement
     * @param param Injection parameters
     * @return ResultSet object
     */
    public ResultSet executeQuery(String sql, Object... param)
    {
        return getSession().executeQuery(sql, param);
    }
    
    /**
     * Obtain CallableStatement object
     * 
     * @param sql SQL statement
     * @return CallableStatement object
     */
    public CallableStatement prepareCall(String sql)
    {
        return getSession().prepareCall(sql);
    }
    
    /**
     * Execute query entities
     * 
     * @param <T> <T>
     * @param c entity class
     * @param sql SQL statement manual cascade
     * @param params Injection parameters
     * @return entities list
     */
    public <T> List<T> queryList(Class<T> c, String sql, Object... params)
    {
        return getSession().queryList(c, sql, params);
    }
    
    /**
     * Execute query entities
     * 
     * @param <T> <T>
     * @param c entity class
     * @param cacheId data cache id
     * @param sql SQL statement manual cascade
     * @param params Injection parameters
     * @return entities list
     */
    public <T> List<T> queryCacheList(Class<T> c, String cacheId, String sql, Object... params)
    {
        return getSession().queryList(c, cacheId, sql, params);
    }
    
    /**
     * Through the result set of query execution
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param rs ResultSet object
     * @param sql SQL statement
     * @param params Injection parameters
     * @return Entities list
     */
    public <T> List<T> queryList(Class<T> c, ResultSet rs, String sql, Object... params)
    {
        return getSession().queryList(c, rs, sql, params);
    }
    
    /**
     * Through the result set of query execution
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param cacheId data cache id
     * @param rs ResultSet object
     * @param sql SQL statement
     * @param params Injection parameters
     * @return Entities list
     */
    public <T> List<T> queryCacheList(Class<T> c, String cacheId, ResultSet rs, String sql, Object... params)
    {
        return getSession().queryList(c, cacheId, rs, sql, params);
    }
    
    /**
     * Execute query entities page list
     * 
     * @param <T> <T>
     * @param c entity class
     * @param page Current page
     * @param pageSize Page size
     * @param sql SQL statement manual cascade
     * @param param Injection parameters
     * @return entities list
     */
    public <T> Page<T> queryPageList(Class<T> c, String sql, int page, int pageSize, Object... param)
    {
        return getSession().queryPageList(c, sql, page, pageSize, param);
    }
    
    /**
     * Execute query entities page list
     * 
     * @param <T> <T>
     * @param c entity class
     * @param cacheId data cache id
     * @param page Current page
     * @param pageSize Page size
     * @param sql SQL statement manual cascade
     * @param param Injection parameters
     * @return {@link Page}
     */
    public <T> Page<T> queryCachePageList(Class<T> c, String cacheId, String sql, int page, int pageSize, Object... param)
    {
        return getSession().queryCachePageList(c, cacheId, sql, page, pageSize, param);
    }
    
    /**
     * Through the primary key value query single entity
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param id Primary key
     * @return Entity object
     */
    public <T> T queryById(Class<T> c, int id)
    {
        return getSession().queryById(c, id);
    }
    
    /**
     * Through the SQL language query single entity
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Entity object
     */
    public <T> T queryBySQL(Class<T> c, String sql, Object... param)
    {
        return getSession().queryBySQL(c, sql, param);
    }
    
    /**
     * Through the SQL language query single entity
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param rs ResultSet object
     * @param sql SQL statement
     * @return Entity object
     */
    public <T> T queryBySQL(Class<T> c, ResultSet rs, String sql)
    {
        return getSession().queryBySQL(c, rs, sql);
    }
    
    /**
     * Through the SQL language query single entity
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param cacheId Cache id
     * @param rs ResultSet object
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Entity object
     */
    public <T> T queryCacheBySQL(Class<T> c, String cacheId, ResultSet rs, String sql, Object... param)
    {
        return getSession().queryBySQL(c, cacheId, rs, sql, param);
    }
    
    /**
     * Through the SQL language query single entity
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param cacheId Data cache id
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Entity object
     */
    public <T> T queryCacheBySQL(Class<T> c, String cacheId, String sql, Object... param)
    {
        return getSession().queryBySQL(c, cacheId, sql, param);
    }
    
    /**
     * Multi table query entities list
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param sql SQL statement
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entity objects list
     */
    public <T> List<T> queryCascadeList(Class<T> c, String sql, String[] entityName, String[] alias, Object... param)
    {
        return getSession().queryCascadeList(c, sql, entityName, alias, param);
    }
    
    /**
     * Query entities list in manual cascade way
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param sql SQL statement
     * @param entityNames Entity's attribute name
     * @param aliases Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> List<T> queryCascadeList(Class<T> c, String sql, String entityNames, String aliases, Object... param)
    {
        return getSession().queryCascadeList(c, sql, entityNames, aliases, param);
    }
    
    /**
     * Query entities list in manual cascade way
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param cacheId Data cache id
     * @param sql SQL statement
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> List<T> queryCacheCascadeList(Class<T> c, String cacheId, String sql, String[] entityName, String[] alias,
        Object... param)
    {
        return getSession().queryCacheCascadeList(c, cacheId, sql, entityName, alias, param);
    }
    
    /**
     * Multi table query single entity
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param sql SQL statement
     * @param param Injection parameters
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @return entity object
     */
    public <T> T queryCascadeObject(Class<T> c, String sql, String[] entityName, String[] alias, Object... param)
    {
        return getSession().queryCascadeObject(c, sql, entityName, alias, param);
    }
    
    /**
     * Query single entity in manual cascade way
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param sql SQL statement
     * @param entityNames Entity's attribute name
     * @param aliases Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> T queryCascadeObject(Class<T> c, String sql, String entityNames, String aliases, Object... param)
    {
        return getSession().queryCascadeObject(c, sql, entityNames, aliases, param);
    }
    
    /**
     * Query single entity in manual cascade way
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param cacheId Data cache id
     * @param sql SQL statement
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> T queryCacheCascadeObject(Class<T> c, String cacheId, String sql, String[] entityName, String[] alias,
        Object... param)
    {
        return getSession().queryCacheCascadeObject(c, cacheId, sql, entityName, alias, param);
    }
    
    /**
     * Multi table query page list
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param sql SQL statement
     * @param page Current page
     * @param pageSize Page size
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return {@link Page}
     */
    public <T> Page<T> queryCascadePageList(Class<T> c, String sql, int page, int pageSize, String[] entityName,
        String[] alias, Object... param)
    {
        return getSession().queryCascadePageList(c, sql, page, pageSize, entityName, alias, param);
    }
    
    /**
     * Query entities page list in manual cascade way
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param sql SQL statement
     * @param page Current page
     * @param pageSize Page size
     * @param entityNames Entity's attribute name
     * @param aliases Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> Page<T> queryCascadePageList(Class<T> c, String sql, int page, int pageSize, String entityNames,
        String aliases, Object... param)
    {
        return getSession().queryCascadePageList(c, sql, page, pageSize, entityNames, aliases, param);
    }
    
    /**
     * Query entities page list in manual cascade way
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param cacheId Data cache id
     * @param sql SQL statement
     * @param page Current page
     * @param pageSize Page size
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> Page<T> queryCacheCascadePageList(Class<T> c, String cacheId, String sql, int page, int pageSize,
        String[] entityName, String[] alias, Object... param)
    {
        return getSession().queryCacheCascadePageList(c, cacheId, sql, page, pageSize, entityName, alias, param);
    }
    
    /**
     * Save entity object
     * 
     * @param <T> <T>
     * @param entity entity object
     * @return Effect of the number of records
     * @throws SQLException Sql exception
     */
    public <T> int save(T entity)
        throws SQLException
    {
        return getSession().save(entity);
    }
    
    /**
     * Save entity object
     * 
     * @param <T> <T>
     * @param entity entity object
     * @param isReturnPk Return the primary key value
     * @return Primary key value
     * @throws SQLException Sql exception
     */
    public <T> Object save(T entity, boolean isReturnPk)
        throws SQLException
    {
        return getSession().save(entity, isReturnPk);
    }
    
    /**
     * Update entity object
     * 
     * @param <T> <T>
     * @param entity entity object
     */
    public <T> void update(T entity)
    {
        update(entity, false);
    }
    
    /**
     * Update entity object
     * 
     * @param <T> <T>
     * @param entity entity object
     * @param isNullable Whether to allow null values, if the true will ignore
     *            the entity class nullable.
     */
    public <T> void update(T entity, boolean isNullable)
    {
        getSession().update(entity, isNullable);
    }
    
    /**
     * Delete entity object
     * 
     * @param <T> <T>
     * @param entity Entity object
     */
    public <T> void delete(T entity)
    {
        getSession().delete(entity);
    }
    
    /**
     * Delete entity object by primary key value
     * 
     * @param <T> <T>
     * @param c Entity class
     * @param id primary key value
     */
    public <T> void delete(Class<T> c, int id)
    {
        getSession().delete(c, id);
    }
    
    /**
     * Execute Jdbc update
     * 
     * @param sql SQL statement
     * @return Effect of the number of records
     */
    public int executeUpdate(String sql)
    {
        return getSession().executeUpdate(sql);
    }
    
    /**
     * Execute Jdbc update
     * 
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Effect of the number of records
     */
    public int executeUpdate(String sql, Object... param)
    {
        return getSession().executeUpdate(sql, param);
    }
    
    /**
     * Execute Jdbc update, and returns the primary key value
     * 
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Primary key value
     */
    public Object executeUpdateGeneratedKey(String sql, Object... param)
    {
        return getSession().executeUpdateGeneratedKey(sql, param);
    }
    
    /**
     * Whether the automatic commit transaction
     * 
     * @return true/false
     */
    public boolean isAutoCommit()
    {
        return getSession().isAutoCommit();
    }
    
    /**
     * <Set automate commit>
     * 
     * @param isAutoCommit Whether automate commit
     * @throws SQLException SQLException
     */
    public void setAutoCommit(boolean isAutoCommit)
        throws SQLException
    {
        getSession().setAutoCommit(isAutoCommit);
    }
    
    /**
     * Commit transaction
     * 
     * @throws SQLException Sql excetpion
     */
    public void commit()
        throws SQLException
    {
        getSession().commit();
    }
    
    /**
     * Rollback transaction
     * 
     * @throws SQLException Sql excetpion
     */
    public void rollback()
        throws SQLException
    {
        getSession().rollback();
    }
    
    /**
     * Rollback transaction to savepoint
     * 
     * @param point Savepoint object
     * @throws SQLException Sql excetpion
     */
    public void rollback(Savepoint point)
        throws SQLException
    {
        getSession().rollback(point);
    }
    
    /**
     * Set save point
     * 
     * @return Savepoint object
     * @throws SQLException Sql excetpion
     */
    public Savepoint setSavepoint()
        throws SQLException
    {
        return getSession().setSavepoint();
    }
    
    /**
     * Set save point by name
     * 
     * @param name save point name
     * @return Savepoint object
     * @throws SQLException Sql excetpion
     */
    public Savepoint setSavepoint(String name)
        throws SQLException
    {
        return getSession().setSavepoint(name);
    }
    
    /**
     * Obtaion SqlMap object by interface class
     * 
     * @param <T> <T>
     * @param c Interface class
     * @return SqlMap object
     */
    public <T> T getSqlMap(Class<T> c)
    {
        return getSession().getSqlMap(c);
    }
    
    /**
     * Obtaion SqlMap object by interface class
     * 
     * @return SqlMapFactory object
     * @see darks.orm.app.factory.SqlMapFactory
     */
    public SqlMapFactory getSqlMapFactory()
    {
        return getSession().getSqlMapFactory();
    }
    
}
