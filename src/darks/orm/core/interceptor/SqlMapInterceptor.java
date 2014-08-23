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

package darks.orm.core.interceptor;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import darks.orm.annotation.sqlmap.Query;
import darks.orm.annotation.sqlmap.Update;
import darks.orm.app.QueryEnumType;
import darks.orm.app.SqlSession;
import darks.orm.core.data.xml.DMLData;
import darks.orm.core.data.xml.DMLData.DMLType;
import darks.orm.core.data.xml.DMLQueryData;
import darks.orm.core.data.xml.InterfaceMethodData;
import darks.orm.core.executor.SqlMapExecutor;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.factory.ExecutorFactory;
import darks.orm.core.factory.SqlMapSingletonFactory;
import darks.orm.exceptions.SqlMapQueryException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.SqlHelper;

@SuppressWarnings("unchecked")
public class SqlMapInterceptor implements MethodInterceptor
{
    
    private static final Logger logger = LoggerFactory.getLogger(SqlMapInterceptor.class);
    
    private SqlSession session;
    
    private Class cls;
    
    public SqlMapInterceptor()
    {
        
    }
    
    public SqlMapInterceptor(SqlSession session, Class cls)
    {
        this.session = session;
        this.cls = cls;
    }
    
    private SqlSession getSession()
        throws Exception
    {
        return session;
    }
    
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy)
        throws Throwable
    {
        Query query = method.getAnnotation(Query.class);
        Update update = method.getAnnotation(Update.class);
        if (query != null)
        {
            return this.AnnotationQueryIntercept(obj, method, args, methodProxy);
        }
        else if (update != null)
        {
            return this.AnnotationUpdateIntercept(obj, method, args, methodProxy);
        }
        else
        {
            return this.SqlMapIntercept(obj, method, args, methodProxy);
        }
    }
    
    public Object AnnotationQueryIntercept(Object obj, Method method, Object[] args, MethodProxy methodProxy)
        throws Throwable
    {
        String className = cls.getName();
        String methodName = method.toGenericString();
        String namesp = className + "." + methodName;
        InterfaceMethodData data = ClassFactory.parseInterfaceClass(namesp, method);
        
        int page = -1;
        int pageSize = -1;
        int num = args.length;
        if (data.getPageIndex() >= 0)
        {
            page = (Integer)args[data.getPageIndex()];
            num--;
        }
        if (data.getPageSizeIndex() >= 0)
        {
            pageSize = (Integer)args[data.getPageSizeIndex()];
            num--;
        }
        String sql = data.getSql();
        Object[] params = args;
        if (num != args.length && sql.indexOf('#') < 0)
        {
            params = new Object[num];
            int index = 0;
            if (num > 0)
            {
                for (int i = 0; i < args.length; i++)
                {
                    if (i != data.getPageIndex() && i != data.getPageSizeIndex() && i != data.getValuesIndex())
                    {
                        params[index++] = args[i];
                    }
                }
            }
        }
        params = SqlHelper.buildParams(sql, params);
        sql = SqlHelper.filterSql(sql);
        QueryEnumType queryEnumType = data.getQueryEnumType();
        SqlSession session = getSession();
        SqlMapExecutor exec =
            ExecutorFactory.getSqlMapCascadeQueryExecutor(session,
                data.isAutoCascade(),
                data.getResultClass(),
                sql,
                params,
                queryEnumType,
                data.getCacheId(),
                page,
                pageSize,
                data.isAutoCache(),
                data.getAttribute(),
                data.getAlias());
        return exec.invoke();
    }
    
    public Object AnnotationUpdateIntercept(Object obj, Method method, Object[] args, MethodProxy methodProxy)
        throws Throwable
    {
        String className = cls.getName();
        String methodName = method.getName();
        String namesp = className + "." + methodName;
        InterfaceMethodData data = ClassFactory.parseInterfaceClass(namesp, method);
        String sql = data.getSql();
        Object[] params = SqlHelper.buildParams(sql, args);
        sql = SqlHelper.filterSql(sql);
        
        getSession().executeUpdate(sql, params);
        return null;
    }
    
    public Object SqlMapIntercept(Object obj, Method method, Object[] args, MethodProxy methodProxy)
        throws Throwable
    {
        String className = cls.getName();
        String methodName = method.getName();
        String namesp = className + "." + methodName;
        DMLData dmlData = SqlMapSingletonFactory.getInstance().getDMLData(namesp);
        if (dmlData == null)
        {
            throw new SqlMapQueryException("query xml id '" + namesp + "' does not exists");
        }
        InterfaceMethodData data = ClassFactory.parseInterfaceClass(namesp, method);
        if (dmlData.getType() == DMLType.Update)
        {
            SqlMapSingletonFactory.getInstance().update(getSession(), namesp, args);
            return null;
        }
        DMLQueryData queryData = dmlData.getQueryData();
        if (queryData.getQueryType() == QueryEnumType.Page)
        {
            if (data.getPageIndex() < 0 || data.getPageSizeIndex() < 0)
            {
                throw new SqlMapQueryException("('" + namesp + "') the index of page or pageSize is less than 0");
            }
        }
        int page = -1;
        int pageSize = -1;
        Object[] values = null;
        int num = args.length;
        if (data.getPageIndex() >= 0)
        {
            page = (Integer)args[data.getPageIndex()];
            num--;
        }
        if (data.getPageSizeIndex() >= 0)
        {
            pageSize = (Integer)args[data.getPageSizeIndex()];
            num--;
        }
        if (data.getValuesIndex() >= 0)
        {
            values = (Object[])args[data.getValuesIndex()];
            num--;
        }
        String sql = queryData.getSql(values);
        Object[] params = args;
        if (num != args.length && sql.indexOf('#') < 0)
        {
            params = new Object[num];
            int index = 0;
            if (num > 0)
            {
                for (int i = 0; i < args.length; i++)
                {
                    if (i != data.getPageIndex() && i != data.getPageSizeIndex() && i != data.getValuesIndex())
                    {
                        params[index++] = args[i];
                    }
                }
            }
        }
        return SqlMapSingletonFactory.getInstance().query(getSession(),
            namesp,
            QueryEnumType.List,
            page,
            pageSize,
            values,
            params);
    }
}