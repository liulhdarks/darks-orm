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

package darks.orm.core.executor;

import java.sql.SQLException;

import darks.orm.app.QueryEnumType;
import darks.orm.app.SqlSession;
import darks.orm.core.aspect.AspectQueryContext;
import darks.orm.core.data.xml.DMLData;
import darks.orm.core.data.xml.DMLQueryData;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.core.factory.ExecutorFactory;

public class SqlMapQueryExecutor extends SqlMapExecutorAdapter
{
    
    private DMLData dmlData;
    
    private DMLQueryData queryData;
    
    private QueryAspectWrapper queryWrapper;
    
    private QueryEnumType queryEnumType;
    
    private SqlSession session;
    
    private AspectQueryContext aspectQueryContext;
    
    public SqlMapQueryExecutor()
    {
        
    }
    
    public SqlMapQueryExecutor(DMLData dmlData, DMLQueryData queryData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType, SqlSession session)
    {
        this.dmlData = dmlData;
        this.queryData = queryData;
        this.queryWrapper = queryWrapper;
        this.queryEnumType = queryEnumType;
        this.session = session;
    }
    
    @Override
    public boolean initialize()
    {
        aspectQueryContext = new AspectQueryContext(queryData.getAspectData());
        return true;
    }
    
    @Override
    public boolean afterInvoke()
    {
        if (queryData.getAspectData() != null)
            return aspectQueryContext.afterInvoke(session, queryData.getAspectData(), queryWrapper, queryEnumType);
        return true;
    }
    
    @Override
    public Object bodyInvoke()
        throws SQLException
    {
        String sql = queryWrapper.getSql();
        Object[] params = queryWrapper.getParams();
        
        int page = queryWrapper.getPage();
        int pageSize = queryWrapper.getPageSize();
        
        boolean autoCascade = true;
        if (queryWrapper.getAutoCascade() <= 0)
            autoCascade = false;
        
        SqlMapExecutor exec =
            ExecutorFactory.getSqlMapCascadeQueryExecutor(session,
                autoCascade,
                queryData.getResultClass(),
                sql,
                params,
                queryEnumType,
                dmlData.getCacheId(),
                page,
                pageSize,
                dmlData.isAutoCache(),
                queryWrapper.getAttribute(),
                queryWrapper.getAlias());
        Object obj = exec.invoke();
        queryWrapper.setResult(obj);
        return obj;
    }
    
    @Override
    public boolean beforeInvoke()
    {
        if (queryData.getAspectData() != null)
            return aspectQueryContext.beforeInvoke(session, queryData.getAspectData(), queryWrapper, queryEnumType);
        return true;
    }
    
    public DMLData getDmlData()
    {
        return dmlData;
    }
    
    public void setDmlData(DMLData dmlData)
    {
        this.dmlData = dmlData;
    }
    
    public DMLQueryData getQueryData()
    {
        return queryData;
    }
    
    public void setQueryData(DMLQueryData queryData)
    {
        this.queryData = queryData;
    }
    
    public QueryAspectWrapper getSqlMapAspectData()
    {
        return queryWrapper;
    }
    
    public void setSqlMapAspectData(QueryAspectWrapper queryWrapper)
    {
        this.queryWrapper = queryWrapper;
    }
    
    public QueryEnumType getQueryEnumType()
    {
        return queryEnumType;
    }
    
    public void setQueryEnumType(QueryEnumType queryEnumType)
    {
        this.queryEnumType = queryEnumType;
    }
    
    public SqlSession getSession()
    {
        return session;
    }
    
    public void setSession(SqlSession session)
    {
        this.session = session;
    }
    
    public AspectQueryContext getAspectQueryContext()
    {
        return aspectQueryContext;
    }
    
    public void setAspectQueryContext(AspectQueryContext aspectQueryContext)
    {
        this.aspectQueryContext = aspectQueryContext;
    }
    
}