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

package darks.orm.app.factory;

import java.util.List;

import darks.orm.app.Page;
import darks.orm.app.QueryEnumType;
import darks.orm.app.SqlSession;
import darks.orm.core.factory.SqlMapSingletonFactory;
import darks.orm.exceptions.SqlMapQueryException;

public class GenericSqlMapFactory implements SqlMapFactory
{
    
    private SqlSession dao = null;
    
    public GenericSqlMapFactory(SqlSession dao)
    {
        this.dao = dao;
    }
    
    /**
     * Execute DDL statement
     */
    public void executeDDLMap()
    {
        SqlMapSingletonFactory sqlmapFactory = SqlMapSingletonFactory.getInstance();
        sqlmapFactory.executeDDLMap();
    }
    
    /**
     * SQLMAP update
     * 
     * @param id SQLMAP id
     * @param params Inject parameters
     */
    public void update(String id, Object... params)
        throws Exception
    {
        SqlMapSingletonFactory sqlmapFactory = SqlMapSingletonFactory.getInstance();
        sqlmapFactory.update(dao, id, params);
    }
    
    /**
     * Query object by SqlMap way according the {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * 
     * @param id SQLMAP id
     * @param queryEnumType {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * @param params Inject parameters
     * @return Result object
     * @throws Exception
     */
    public Object query(String id, QueryEnumType queryEnumType, Object... params)
        throws Exception
    {
        return query(id, queryEnumType, null, params);
    }
    
    /**
     * Query object by SqlMap way according the {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * 
     * @param id SQLMAP id
     * @param queryEnumType {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * @param values Select type values. Used for <select> <constitute> tag type.
     * @param params Inject parameters
     * @return Result object
     * @throws Exception
     */
    public Object query(String id, QueryEnumType queryEnumType, Object[] values, Object[] params)
        throws SqlMapQueryException
    {
        if (queryEnumType == QueryEnumType.Page || queryEnumType == QueryEnumType.Auto)
        {
            throw new SqlMapQueryException("GenericSqlMapFactory::query queryEnumType can not use Page/Auto,please change to another query method with page/pageSize");
        }
        SqlMapSingletonFactory sqlmapFactory = SqlMapSingletonFactory.getInstance();
        return sqlmapFactory.query(dao, id, queryEnumType, 0, 0, values, params);
    }
    
    /**
     * Query object by SqlMap way according the {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * 
     * @param id SQLMAP id
     * @param queryEnumType {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * @param values Select type values. Used for <select> <constitute> tag type.
     * @param params Inject parameters
     * @param page Current page number
     * @param pageSize Page size each page
     * @return Result object
     * @throws Exception
     */
    public Object query(String id, QueryEnumType queryEnumType, int page, int pageSize, Object[] values, Object[] params)
        throws Exception
    {
        SqlMapSingletonFactory sqlmapFactory = SqlMapSingletonFactory.getInstance();
        return sqlmapFactory.query(dao, id, queryEnumType, page, pageSize, values, params);
    }
    
    /**
     * SQLMAP query single obejct
     * 
     * @param id SQLMAP id
     * @param params Inject parameters
     * @return Result object
     * @throws Exception
     */
    public Object queryObject(String id, Object... params)
        throws Exception
    {
        return queryObject(id, null, params);
    }
    
    /**
     * SQLMAP query single obejct
     * 
     * @param id SQLMAP id
     * @param values Select type values.Used for <select> <constitute> tags
     * @param params Inject parameters
     * @return Result object
     * @throws Exception
     */
    public Object queryObject(String id, Object[] values, Object[] params)
        throws Exception
    {
        return queryForType(QueryEnumType.Object, id, values, params);
    }
    
    /**
     * SQLMAP query list object
     * 
     * @param id SQLMAP id
     * @param params Inject parameters
     * @return List result
     * @throws Exception
     */
    public List<?> queryList(String id, Object... params)
        throws Exception
    {
        return (List<?>)queryForType(QueryEnumType.List, id, null, params);
    }
    
    /**
     * SQLMAP query list object
     * 
     * @param id SQLMAP id
     * @param values Select type values.Used for<select> <constitute> tag
     * @param params Inject parameters
     * @return List result
     * @throws Exception
     */
    public List<?> queryList(String id, Object[] values, Object[] params)
        throws Exception
    {
        return (List<?>)queryForType(QueryEnumType.List, id, values, params);
    }
    
    /**
     * SQLMAP query page 
     * 
     * @param id SQLMAP id
     * @param params Inject parameters
     * @return Page result object
     * @throws Exception
     */
    public Page<?> queryPageList(String id, int page, int pageSize, Object... params)
        throws Exception
    {
        return queryPageList(id, page, pageSize, null, params);
    }
    
    /**
     * SQLMAP query page 
     * 
     * @param id SQLMAP id
     * @param values Select type values.Used for<select> <constitute> tag
     * @param params Inject parameters
     * @return Page result object
     * @throws Exception
     */
    public Page<?> queryPageList(String id, int page, int pageSize, Object[] values, Object[] params)
        throws Exception
    {
        return (Page<?>)queryForType(QueryEnumType.Page, id, page, pageSize, values, params);
    }
    
    /**
     * Query data by SQLMAP way according the {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * 
     * @param queryEnumType {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * @param id SQLMAP id
     * @param params Inject parameters
     * @return Result object
     * @throws Exception
     */
    public Object queryForType(QueryEnumType queryEnumType, String id, Object[] params)
        throws Exception
    {
        return queryForType(queryEnumType, id, 0, 0, null, params);
    }
    
    /**
     * Query data by SQLMAP way according the {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * 
     * @param queryEnumType {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * @param id SQLMAP id
     * @param values Select type values.Used for <select> <constitute> tag
     * @param params Inject parameters
     * @return Result object
     * @throws Exception
     */
    public Object queryForType(QueryEnumType queryEnumType, String id, Object[] values, Object[] params)
        throws Exception
    {
        return queryForType(queryEnumType, id, 0, 0, values, params);
    }
    
    /**
     * Query data by SQLMAP way according the {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * 
     * @param queryEnumType {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * @param id SQLMAP id
     * @param page Current page
     * @param pageSize Page size
     * @param values Select type values.Used for <select> <constitute> tags
     * @param params Inject parameters
     * @return Result object
     * @throws Exception
     */
    public Object queryForType(QueryEnumType queryEnumType, String id, int page, int pageSize, Object[] values,
        Object[] params)
        throws Exception
    {
        SqlMapSingletonFactory sqlmapFactory = SqlMapSingletonFactory.getInstance();
        return sqlmapFactory.queryForType(dao, queryEnumType, id, page, pageSize, values, params);
    }
    
    public SqlSession getDao()
    {
        return dao;
    }
    
    public void setDao(SqlSession dao)
    {
        this.dao = dao;
    }
    
}