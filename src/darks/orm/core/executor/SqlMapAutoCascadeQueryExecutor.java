
package darks.orm.core.executor;

import java.sql.SQLException;

import darks.orm.app.QueryEnumType;
import darks.orm.app.SqlSession;
import darks.orm.core.factory.SqlSessionFactory;
import darks.orm.exceptions.SqlMapQueryException;

public class SqlMapAutoCascadeQueryExecutor extends SqlMapExecutorAdapter
{
    
    private SqlSession session;
    
    private Class<?> resultClass;
    
    private String sql;
    
    private Object[] params;
    
    private QueryEnumType queryEnumType;
    
    private String cacheId;
    
    private int page;
    
    private int pageSize;
    
    private boolean autoCache;
    
    public SqlMapAutoCascadeQueryExecutor()
    {
        
    }
    
    public SqlMapAutoCascadeQueryExecutor(SqlSession session, Class<?> resultClass, String sql, Object[] params,
        QueryEnumType queryEnumType, String cacheId, int page, int pageSize, boolean autoCache)
    {
        this.session = session;
        this.resultClass = resultClass;
        this.sql = sql;
        this.params = params;
        this.queryEnumType = queryEnumType;
        this.cacheId = cacheId;
        this.page = page;
        this.pageSize = pageSize;
        this.autoCache = autoCache;
    }
    
    @Override
    public Object bodyInvoke()
        throws SQLException
    {
        boolean isClose = false;
        if (session == null || (session.isInited() && session.isClosed()))
        {
            // 获得SQL会话
            session = SqlSessionFactory.getSession(true);
            isClose = true;
        }
        try
        {
            if (cacheId == null || "".equals(cacheId))
                autoCache = true;
            if (queryEnumType == QueryEnumType.Object)
            { // 查询对象
                if (autoCache) // 自动缓存
                    return session.queryBySQL(resultClass, sql, params);
                else
                    return session.queryBySQL(resultClass, cacheId, sql, params);
            }
            else if (queryEnumType == QueryEnumType.List || queryEnumType == QueryEnumType.Auto)
            { // 查询列表,自动查询默认为列表查询
                if (autoCache) // 自动缓存
                    return session.queryList(resultClass, sql, params);
                else
                    return session.queryList(resultClass, cacheId, sql, params);
            }
            else if (queryEnumType == QueryEnumType.Page)
            { // 查询分页列表
                if (autoCache) // 自动缓存
                    return session.queryPageList(resultClass, sql, page, pageSize, params);
                else
                    return session.queryCachePageList(resultClass, cacheId, sql, page, pageSize, params);
            }
        }
        catch (Exception e)
        {
            throw new SqlMapQueryException(e);
        }
        finally
        {
            if (isClose)
                session.close();
        }
        return null;
    }
    
}
