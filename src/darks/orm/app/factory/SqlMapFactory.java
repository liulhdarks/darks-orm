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
import darks.orm.app.SqlSession;
import darks.orm.app.QueryEnumType;

public interface SqlMapFactory
{
    
    /**
     * 执行DDL语句
     */
    public void executeDDLMap();
    
    /**
     * SQLMAP更新
     * 
     * @param id SQLMAP编号
     * @param params 注入参数(不定)
     */
    public void update(String id, Object... params)
        throws Exception;
    
    /**
     * 根据查询结果类型进行SQLMAP查询
     * 
     * @param id SQLMAP编号
     * @param queryEnumType 查询结果类型枚举
     * @param params 注入参数(不定)
     * @return 对象
     * @throws Exception
     */
    public Object query(String id, QueryEnumType queryEnumType, Object... params)
        throws Exception;
    
    /**
     * 根据查询结果类型进行SQLMAP查询
     * 
     * @param id SQLMAP编号
     * @param queryEnumType 查询结果类型枚举
     * @param values 选择类型值,用于<select> <constitute>标签类型
     * @param params 注入参数(数组)
     * @return 对象
     * @throws Exception
     */
    public Object query(String id, QueryEnumType queryEnumType, Object[] values, Object[] params)
        throws Exception;
    
    /**
     * 根据查询结果类型进行SQLMAP查询
     * 
     * @param id SQLMAP编号
     * @param queryEnumType 查询结果类型枚举
     * @param values 选择类型值,用于<select> <constitute>标签类型
     * @param params 注入参数(数组)
     * @param page 当前页数
     * @param pageSize 分页大小
     * @return 对象
     * @throws Exception
     */
    public Object query(String id, QueryEnumType queryEnumType, int page, int pageSize, Object[] values, Object[] params)
        throws Exception;
    
    /**
     * SQLMAP查询单个对象
     * 
     * @param id SQLMAP编号
     * @param params 注入参数(不定)
     * @return 单个对象
     * @throws Exception
     */
    public Object queryObject(String id, Object... params)
        throws Exception;
    
    /**
     * SQLMAP查询单个对象
     * 
     * @param id SQLMAP编号
     * @param values 选择类型值,用于<select> <constitute>标签类型
     * @param params 注入参数(数组)
     * @return 单个对象
     * @throws Exception
     */
    public Object queryObject(String id, Object[] values, Object[] params)
        throws Exception;
    
    /**
     * SQLMAP查询列表对象
     * 
     * @param id SQLMAP编号
     * @param params 注入参数(不定)
     * @return 列表对象
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List queryList(String id, Object... params)
        throws Exception;
    
    /**
     * SQLMAP查询列表对象
     * 
     * @param id SQLMAP编号
     * @param values 选择类型值,用于<select> <constitute>标签类型
     * @param params 注入参数(数组)
     * @return 列表对象
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public List queryList(String id, Object[] values, Object[] params)
        throws Exception;
    
    /**
     * SQLMAP查询分页对象,调用queryPageListA
     * 
     * @param id SQLMAP编号
     * @param params 注入参数(不定)
     * @return 分页对象
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public Page queryPageList(String id, int page, int pageSize, Object... params)
        throws Exception;
    
    /**
     * SQLMAP查询分页对象
     * 
     * @param id SQLMAP编号
     * @param values 选择类型值,用于<select> <constitute>标签类型
     * @param params 注入参数(数组)
     * @return 分页对象
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public Page queryPageList(String id, int page, int pageSize, Object[] values, Object[] params)
        throws Exception;
    
    /**
     * 根据查询类型进行SQLMAP查询
     * 
     * @param queryEnumType 查询类型枚举
     * @param id SQLMAP编号
     * @param params 注入参数
     * @return 查询结果对象
     * @throws Exception
     */
    public Object queryForType(QueryEnumType queryEnumType, String id, Object[] params)
        throws Exception;
    
    /**
     * 根据查询类型进行SQLMAP查询
     * 
     * @param queryEnumType 查询类型枚举
     * @param id SQLMAP编号
     * @param values 选择类型值,用于<select> <constitute>标签类型
     * @param params 注入参数
     * @return 查询结果对象
     * @throws Exception
     */
    public Object queryForType(QueryEnumType queryEnumType, String id, Object[] values, Object[] params)
        throws Exception;
    
    /**
     * 根据查询类型进行SQLMAP查询
     * 
     * @param queryEnumType 查询类型枚举
     * @param id SQLMAP编号
     * @param page 当前页数
     * @param pageSize 分页大小
     * @param values 选择类型值,用于<select> <constitute>标签类型
     * @param params 注入参数
     * @return 查询结果对象
     * @throws Exception
     */
    public Object queryForType(QueryEnumType queryEnumType, String id, int page, int pageSize, Object[] values,
        Object[] params)
        throws Exception;
    
    public SqlSession getDao();
    
    public void setDao(SqlSession dao);
    
}