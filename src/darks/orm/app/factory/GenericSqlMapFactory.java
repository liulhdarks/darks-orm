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
     * 执行DDL语句
     */
    public void executeDDLMap()
    {
        SqlMapSingletonFactory sqlmapFactory = SqlMapSingletonFactory.getInstance();
        sqlmapFactory.executeDDLMap();
    }
    
    /**
     * SQLMAP更新
     * 
     * @param id SQLMAP编号
     * @param params 注入参数(数组)
     */
    public void update(String id, Object... params)
        throws Exception
    {
        SqlMapSingletonFactory sqlmapFactory = SqlMapSingletonFactory.getInstance();
        sqlmapFactory.update(dao, id, params);
    }
    
    /**
     * 根据查询结果类型进行SQLMAP查询
     * 
     * @param id SQLMAP编号
     * @param queryEnumType 查询结果类型枚举
     * @param params 注入参数(数组)
     * @return 对象
     * @throws Exception
     */
    public Object query(String id, QueryEnumType queryEnumType, Object... params)
        throws Exception
    {
        return query(id, queryEnumType, null, params);
    }
    
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
        throws Exception
    {
        SqlMapSingletonFactory sqlmapFactory = SqlMapSingletonFactory.getInstance();
        return sqlmapFactory.query(dao, id, queryEnumType, page, pageSize, values, params);
    }
    
    /**
     * SQLMAP查询单个对象
     * 
     * @param id SQLMAP编号
     * @param params 注入参数(数组)
     * @return 单个对象
     * @throws Exception
     */
    public Object queryObject(String id, Object... params)
        throws Exception
    {
        return queryObject(id, null, params);
    }
    
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
        throws Exception
    {
        return queryForType(QueryEnumType.Object, id, values, params);
    }
    
    /**
     * SQLMAP查询列表对象
     * 
     * @param id SQLMAP编号
     * @param params 注入参数(数组)
     * @return 列表对象
     * @throws Exception
     */
    public List<?> queryList(String id, Object... params)
        throws Exception
    {
        return (List<?>)queryForType(QueryEnumType.List, id, null, params);
    }
    
    /**
     * SQLMAP查询列表对象
     * 
     * @param id SQLMAP编号
     * @param values 选择类型值,用于<select> <constitute>标签类型
     * @param params 注入参数(数组)
     * @return 列表对象
     * @throws Exception
     */
    public List<?> queryList(String id, Object[] values, Object[] params)
        throws Exception
    {
        return (List<?>)queryForType(QueryEnumType.List, id, values, params);
    }
    
    /**
     * SQLMAP查询分页对象
     * 
     * @param id SQLMAP编号
     * @param params 注入参数(数组)
     * @return 分页对象
     * @throws Exception
     */
    public Page<?> queryPageList(String id, int page, int pageSize, Object... params)
        throws Exception
    {
        return queryPageList(id, page, pageSize, null, params);
    }
    
    /**
     * SQLMAP查询分页对象
     * 
     * @param id SQLMAP编号
     * @param values 选择类型值,用于<select> <constitute>标签类型
     * @param params 注入参数(数组)
     * @return 分页对象
     * @throws Exception
     */
    public Page<?> queryPageList(String id, int page, int pageSize, Object[] values, Object[] params)
        throws Exception
    {
        return (Page<?>)queryForType(QueryEnumType.Page, id, page, pageSize, values, params);
    }
    
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
        throws Exception
    {
        return queryForType(queryEnumType, id, 0, 0, null, params);
    }
    
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
        throws Exception
    {
        return queryForType(queryEnumType, id, 0, 0, values, params);
    }
    
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
