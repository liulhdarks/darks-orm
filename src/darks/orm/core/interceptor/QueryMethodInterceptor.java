package darks.orm.core.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import darks.orm.annotation.Query;
import darks.orm.annotation.Query.QueryType;
import darks.orm.app.SqlSession;
import darks.orm.core.data.MethodData;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.factory.SqlSessionFactory;
import darks.orm.exceptions.ClassReflectException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.StringHelper;

/**
 * BEAN方法拦截类 作者:DarkShadow 版权:归夜影所有 时间:2011-12-25 版本:1.0.0
 */
@SuppressWarnings("unchecked")
public class QueryMethodInterceptor implements MethodInterceptor, Serializable
{
    
    private transient static final Logger logger = LoggerFactory.getLogger(QueryMethodInterceptor.class);
    
    private static final long serialVersionUID = 1322619765770865185L;
    
    // private boolean isOwn=false;
    
    public QueryMethodInterceptor()
    {
    }
    
    /**
     * 方法拦截器
     */
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
        throws Throwable
    {
        
        try
        {
            
            Query qy = (Query)method.getAnnotation(Query.class);
            if (qy == null)
            {
                return proxy.invokeSuper(obj, args);
            }
            else
            {
                return this.QueryInterceptor(obj, method, args, proxy);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 自定义查询拦截
     * 
     * @param obj 实体对象
     * @param method 拦截方法
     * @param args 方法参数
     * @param proxy 方法代理
     * @return 返回对象
     * @throws Throwable
     */
    private Object QueryInterceptor(Object obj, Method method, Object[] args, MethodProxy proxy)
        throws Throwable
    {
        
        Query qy = (Query)method.getAnnotation(Query.class);
        String sql = qy.SQL();
        String[] params = qy.paramType();
        Class cobj = obj.getClass();
        
        MethodData methodData = ClassFactory.getMethod(method.toGenericString());
        List<Object> newargs = new ArrayList<Object>(params.length);
        for (String s : params)
        {
            if (s.startsWith("#"))
            { // 若注入参数为方法参数
                int index = getArgumentIndex(methodData, s);
                if (index > 0)
                {
                    newargs.add(args[index - 1]);
                }
                else
                {
                    continue;
                }
            }
            else
            { // 注入参数为实体属性
                String mtName = "get" + StringHelper.upperHeadWord(s);
                Method mt = cobj.getMethod(mtName);
                if (mt == null)
                {
                    throw new ClassReflectException("There is no method named '" + mtName + "' in class '" + cobj + "'");
                }
                Object val = mt.invoke(obj);
                newargs.add(val);
            }
        }
        // 获得会话
        boolean isOwn = false;
        if (SqlSessionFactory.isCurrentSessionClosed())
        {
            isOwn = true;
        }
        SqlSession session = SqlSessionFactory.getSession();
        
        Class c = qy.resultType();
        if (qy.queryType() == QueryType.ListType)
        {
            List list = session.queryList(c, sql, newargs.toArray());
            if (isOwn)
            {
                session.close();
            }
            return list;
        }
        else if (qy.queryType() == QueryType.SingleType)
        {
            Object ob = null;
            ob = session.queryBySQL(c, sql, newargs.toArray());
            if (isOwn)
            {
                session.close();
            }
            return ob;
        }
        
        return null;
    }
    
    /**
     * 获得参数索引
     * 
     * @param methodData 方法数据实例
     * @param param 参数名/参数位置
     * @return 索引
     * @throws Exception
     */
    public int getArgumentIndex(MethodData methodData, String param)
        throws Exception
    {
        try
        {
            int index = -1;
            String argName = param.substring(1);
            if (methodData != null)
            {
                index = methodData.getArgumentIndex(argName);
            }
            if (index < 0)
            {
                index = Integer.parseInt(argName);
            }
            return index;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }
}
