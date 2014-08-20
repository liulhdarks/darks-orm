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

package darks.orm.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import darks.orm.app.Page;
import darks.orm.exceptions.SessionException;

/**
 * SqlBeanSession interface
 * 
 * <p>
 * <h1>SqlBeanSession.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public interface SqlBeanSession extends SqlBaseSession
{
    
    /**
     * Execute query entities
     * 
     * @param c entity class
     * @param sql SQL statement manual cascade
     * @param param Injection parameters
     * @return entities list
     */
    public <T> List<T> queryList(Class<T> c, String sql, Object... params);
    
    /**
     * Through the result set of query execution
     * 
     * @param c Entity class
     * @param rs ResultSet object
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> List<T> queryList(Class<T> c, ResultSet rs, String sql, Object... params);
    
    /**
     * Through the result set of query execution
     * 
     * @param c Entity class
     * @param cacheId data cache id
     * @param rs ResultSet object
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> List<T> queryCacheList(Class<T> c, String cacheId, ResultSet rs, String sql, Object... params);
    
    /**
     * Execute query entities
     * 
     * @param c entity class
     * @param cacheId data cache id
     * @param sql SQL statement manual cascade
     * @param param Injection parameters
     * @return entities list
     */
    public <T> List<T> queryCacheList(Class<T> c, String cacheId, String sql, Object... params);
    
    /**
     * Multi table query single entity
     * 
     * @param c Entity class
     * @param sql SQL statement
     * @param param Injection parameters
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @return entity object
     */
    public <T> T queryCascadeObject(Class<T> c, String sql, String[] entityName, String[] alias, Object... param);
    
    /**
     * Multi table query single entity
     * 
     * @param c Entity class
     * @param sql SQL statement
     * @param param Injection parameters
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @return entity object
     */
    public <T> T queryCascadeObject(Class<T> c, String sql, String entityNames, String aliases, Object... param);
    
    /**
     * Query single entity in manual cascade way
     * 
     * @param c Entity class
     * @param cacheId Data cache id
     * @param sql SQL statement
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> T queryCacheCascadeObject(Class<T> c, String cacheId, String sql, String[] entityName, String[] alias,
        Object... param);
    
    /**
     * Multi table query entities list
     * 
     * @param c Entity class
     * @param sql SQL statement
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entity objects list
     */
    public <T> List<T> queryCascadeList(Class<T> c, String sql, String[] entityName, String[] alias, Object... param);
    
    /**
     * Query entities list in manual cascade way
     * 
     * @param c Entity class
     * @param sql SQL statement
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> List<T> queryCascadeList(Class<T> c, String sql, String entityNames, String aliases, Object... param);
    
    /**
     * Query entities list in manual cascade way
     * 
     * @param c Entity class
     * @param cacheId Data cache id
     * @param sql SQL statement
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> List<T> queryCacheCascadeList(Class<T> c, String cacheId, String sql, String[] entityName, String[] alias,
        Object[] param);
    
    /**
     * Multi table query page list
     * 
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
        String[] alias, Object... param);
    
    /**
     * Query entities page list in manual cascade way
     * 
     * @param c Entity class
     * @param sql SQL statement
     * @param page Current page
     * @param pageSize Page size
     * @param entityName Entity's attribute name
     * @param alias Entity attribute corresponding to the alias
     * @param param Injection parameters
     * @return Entities list
     */
    public <T> Page<T> queryCascadePageList(Class<T> c, String sql, int page, int pageSize, String entityNames,
        String aliases, Object... param);
    
    /**
     * Query entities page list in manual cascade way
     * 
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
        String[] entityName, String[] alias, Object... param);
    
    /**
     * Through the primary key value query single entity
     * 
     * @param c Entity class
     * @param id Primary key
     * @return Entity object
     */
    public <T> T queryById(Class<T> c, int id);
    
    /**
     * Through the SQL language query single entity
     * 
     * @param c Entity class
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Entity object
     */
    public <T> T queryBySQL(Class<T> c, String sql, Object... param);
    
    /**
     * Through the SQL language query single entity
     * 
     * @param c Entity class
     * @param rs ResultSet object
     * @param sql SQL statement
     * @return Entity object
     */
    public <T> T queryBySQL(Class<T> c, ResultSet rs, String sql);
    
    /**
     * Through the SQL language query single entity
     * 
     * @param c Entity class
     * @param cacheId Data cache id
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Entity object
     */
    public <T> T queryCacheBySQL(Class<T> c, String cacheId, String sql, Object... param);
    
    /**
     * Through the SQL language query single entity
     * 
     * @param c Entity class
     * @param rs ResultSet object
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Entity object
     */
    public <T> T queryCacheBySQL(Class<T> c, String cacheId, ResultSet rs, String sql, Object... param);
    
    /**
     * Execute query entities page list
     * 
     * @param c entity class
     * @param page Current page
     * @param pageSize Page size
     * @param sql SQL statement manual cascade
     * @param param Injection parameters
     * @return entities list
     */
    public <T> Page<T> queryPageList(Class<T> c, String sql, int page, int pageSize, Object... param);
    
    /**
     * Execute query entities page list
     * 
     * @param c entity class
     * @param cacheId data cache id
     * @param page Current page
     * @param pageSize Page size
     * @param sql SQL statement manual cascade
     * @param param Injection parameters
     * @return {@link Page}
     */
    public <T> Page<T> queryCachePageList(Class<T> c, String cacheId, String sql, int page, int pageSize, Object... param);
    
    /**
     * Save entity object
     * 
     * @param entity entity object
     * @return Effect of the number of records
     */
    public <T> int save(T entity)
        throws SQLException;
    
    /**
     * Save entity object
     * 
     * @param entity entity object
     * @param isReturnPk Return the primary key value
     * @return Primary key value
     */
    public <T> Object save(T entity, boolean isReturnPk)
        throws SQLException;
    
    /**
     * Update entity object
     * 
     * @param entity entity object
     */
    public <T> void update(T entity)
        throws SessionException;
    
    /**
     * Update entity object
     * 
     * @param entity entity object
     * @param isNullable Whether to allow null values, if the true will ignore
     *            the entity class nullable.
     */
    public <T> void update(T entity, boolean isNullable)
        throws SessionException;
    
    /**
     * Delete entity object
     * 
     * @param entity Entity object
     */
    public <T> void delete(T entity)
        throws SessionException;
    
    /**
     * Delete entity object by primary key value
     * 
     * @param c Entity class
     * @param id primary key value
     */
    public <T> void delete(Class<T> c, int id)
        throws SessionException;
}
