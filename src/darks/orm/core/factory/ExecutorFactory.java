
package darks.orm.core.factory;

import darks.orm.app.QueryEnumType;
import darks.orm.app.SqlSession;
import darks.orm.core.data.xml.DMLData;
import darks.orm.core.data.xml.DMLQueryData;
import darks.orm.core.data.xml.DMLUpdateData;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.core.data.xml.SimpleAspectWrapper;
import darks.orm.core.executor.SqlMapAutoCascadeQueryExecutor;
import darks.orm.core.executor.SqlMapExecutor;
import darks.orm.core.executor.SqlMapManualCascadeQueryExecutor;
import darks.orm.core.executor.SqlMapQueryExecutor;
import darks.orm.core.executor.SqlMapUpdateExecutor;

public final class ExecutorFactory
{
    
    private ExecutorFactory()
    {
        
    }
    
    /**
     * 获得SqlMap更新执行器
     * 
     * @param session SQL会话
     * @param updateData 更新数据
     * @param sqlMapAspectData 方面数据
     * @return
     */
    public static final SqlMapExecutor getSqlMapUpdateExecutor(SqlSession session, DMLUpdateData updateData,
        SimpleAspectWrapper simpleWrapper)
    {
        return new SqlMapUpdateExecutor(session, updateData, simpleWrapper);
    }
    
    /**
     * 获得SqlMap查询执行器
     * 
     * @param session SQL会话
     * @param dmlData DML数据
     * @param queryData 查询数据
     * @param sqlMapAspectData 方面数据
     * @param queryEnumType 查询类型
     * @return
     */
    public static final SqlMapExecutor getSqlMapQueryExecutor(SqlSession session, DMLData dmlData,
        DMLQueryData queryData, QueryAspectWrapper queryWrapper, QueryEnumType queryEnumType)
    {
        
        return new SqlMapQueryExecutor(dmlData, queryData, queryWrapper, queryEnumType, session);
    }
    
    // executeSqlMapQuery(SqlSession, Class, String, Object[], QueryEnumType,
    // String, String, String, int, int, boolean, boolean)
    /**
     * 获得级联查询执行器
     * 
     * @param isAutoCascade
     * @param session
     * @param resultClass
     * @param sql
     * @param params
     * @param queryEnumType
     * @param cacheId
     * @param page
     * @param pageSize
     * @param autoCache
     * @param attr
     * @param alias
     * @return
     */
    public static final SqlMapExecutor getSqlMapCascadeQueryExecutor(SqlSession session, boolean isAutoCascade,
        Class<?> resultClass, String sql, Object[] params, QueryEnumType queryEnumType, String cacheId, int page,
        int pageSize, boolean autoCache, String attr, String alias)
    {
        if (isAutoCascade)
        {
            return new SqlMapAutoCascadeQueryExecutor(session, resultClass, sql, params, queryEnumType, cacheId, page,
                pageSize, autoCache);
        }
        return new SqlMapManualCascadeQueryExecutor(session, resultClass, sql, params, queryEnumType, cacheId, page,
            pageSize, autoCache, attr, alias);
    }
}
