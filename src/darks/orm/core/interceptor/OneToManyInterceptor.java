package darks.orm.core.interceptor;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import darks.orm.annotation.Column;
import darks.orm.annotation.MappedType;
import darks.orm.annotation.OneToMany;
import darks.orm.app.SqlSession;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.factory.SqlSessionFactory;
import darks.orm.util.ReflectHelper;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * BEAN方法拦截类 作者:DarkShadow 版权:归夜影所有 时间:2011-12-25 版本:1.0.0
 */
@SuppressWarnings("unchecked")
public class OneToManyInterceptor implements MethodInterceptor, Serializable
{
    
    private static final long serialVersionUID = -8188501749841675035L;
    
    public OneToManyInterceptor()
    {
    }
    
    /**
     * 一对多关系拦截
     * 
     * @param obj 实体对象
     * @param method 拦截方法
     * @param args 方法参数
     * @param proxy 方法代理
     * @return 返回对象
     * @throws Throwable
     */
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
        throws Throwable
    {
        
        OneToMany om = (OneToMany)method.getAnnotation(OneToMany.class);
        if (om == null)
        {
            return proxy.invokeSuper(obj, args);
        }
        List list = (List)proxy.invokeSuper(obj, args);
        
        Class pkc = obj.getClass();
        Class c = om.resultType();
        if (c == null)
        {
            ParameterizedType pt = (ParameterizedType)method.getGenericReturnType();
            c = (Class)pt.getActualTypeArguments()[0];
        }
        String sql = null;
        Object val = null;
        if (om.mappedType() == MappedType.EntityType)
        {
            String fn = om.mappedBy();
            if ("".equals(fn))
                return null;
            
            String fs = ClassFactory.getFieldNames(c);
            String tn = ClassFactory.getTableName(c);
            val = ClassFactory.getPrimaryKeyValue(pkc, obj);
            if (val == null)
                return null;
            Field f = null;
            
            f = c.getDeclaredField(fn);
            String dbf = null;
            Column cl = (Column)f.getAnnotation(Column.class);
            if (cl == null)
            {
                dbf = f.getName();
            }
            else
            {
                dbf = cl.value();
            }
            StringBuffer buf = new StringBuffer();
            buf.append("select ");
            buf.append(fs);
            buf.append(" from ");
            buf.append(tn);
            buf.append(" where ");
            buf.append(dbf);
            buf.append(" = ?");
            sql = buf.toString();
            
        }
        else if (om.mappedType() == MappedType.SqlType)
        {
            sql = om.SQL();
            if ("".equals(sql))
                return null;
            val = ClassFactory.getPrimaryKeyValue(pkc, obj);
        }
        
        // 查询数据--------------------------------------------
        boolean isOwn = false;
        if (SqlSessionFactory.isCurrentSessionClosed())
        {
            isOwn = true;
        }
        SqlSession session = null;
        try
        {
            session = SqlSessionFactory.getSession();
            list = session.queryList(c, sql, new Object[] {val});
        }
        finally
        {
            if (isOwn)
            {
                session.close();
            }
        }
        
        String mn = method.getName();
        mn = mn.replaceFirst("get", "set");
        Class cobj = obj.getClass();
        Method mt = ReflectHelper.getAllMethod(cobj, mn, method.getReturnType());
        //cobj.getMethod(mn, new Class[] { method.getReturnType() });
        mt.invoke(obj, list);
        
        return list;
    }
    
}
