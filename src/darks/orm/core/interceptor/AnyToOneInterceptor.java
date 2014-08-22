package darks.orm.core.interceptor;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import darks.orm.annotation.Column;
import darks.orm.annotation.ManyToOne;
import darks.orm.annotation.MappedType;
import darks.orm.annotation.OneToOne;
import darks.orm.app.SqlSession;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.factory.SqlSessionFactory;
import darks.orm.exceptions.SessionException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.ReflectHelper;

/**
 * BEAN方法拦截类 作者:DarkShadow 版权:归夜影所有 时间:2011-12-25 版本:1.0.0
 */
@SuppressWarnings("unchecked")
public class AnyToOneInterceptor implements MethodInterceptor, Serializable
{
    
    private transient static final Logger logger = LoggerFactory.getLogger(QueryMethodInterceptor.class);
    
    private static final long serialVersionUID = -3811271751073729233L;
    
    // private boolean isOwn=false;
    
    public AnyToOneInterceptor()
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
            String sql = null;
            String fn = null;
            Class c = null;
            boolean type = false;
            ManyToOne mo = (ManyToOne)method.getAnnotation(ManyToOne.class);
            if (mo == null)
            {
                OneToOne oo = (OneToOne)method.getAnnotation(OneToOne.class);
                if (oo == null)
                {
                    return proxy.invokeSuper(obj, args);
                }
                else
                {
                    sql = oo.SQL();
                    if (oo.mappedType() == MappedType.EntityType)
                    {
                        type = true;
                        fn = oo.mappedBy();
                        c = oo.resultType();
                    }
                }
            }
            else
            {
                sql = mo.SQL();
                if (mo.mappedType() == MappedType.EntityType)
                {
                    type = true;
                    fn = mo.mappedBy();
                    c = mo.resultType();
                }
            }
            if (type)
            {
                if ("".equals(fn))
                    return null;
                Class pkc = obj.getClass();
                Object val = null;
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
            
            return this.SomeToOneMethodInterceptor(sql, obj, method, args, proxy);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 一对一、多对一关系拦截
     * 
     * @param obj 实体对象
     * @param method 拦截方法
     * @param args 方法参数
     * @param proxy 方法代理
     * @return 返回对象
     * @throws Throwable
     */
    private Object SomeToOneMethodInterceptor(String sql, Object obj, Method method, Object[] args, MethodProxy proxy)
        throws Throwable
    {
        
        Class cobj = obj.getClass();
        Object ob = proxy.invokeSuper(obj, args);
        // System.out.println(method.getName()+"处理more对one");
        if (ob != null)
            return ob;
        String mn = method.getName();
        String mnfk = mn.replaceFirst("get", "getFk_");
        
        Method mnf = cobj.getMethod(mnfk);
        Object val = mnf.invoke(obj);
        
        Class c = method.getReturnType();
        // 获得会话
        boolean isOwn = false;
        if (SqlSessionFactory.isCurrentSessionClosed())
        {
            isOwn = true;
        }
        SqlSession session = SqlSessionFactory.getSession();
        if (session == null)
        {
            throw new SessionException("failed to create session");
        }
        ob = session.queryBySQL(c, sql, val);
        if (isOwn)
        {
            session.close();
        }
        if (ob == null)
            return null;
        
        mn = mn.replaceFirst("get", "set");
        
        Method mt = ReflectHelper.getAllMethod(cobj, mn, method.getReturnType());
        //cobj.getMethod(mn, new Class[] { method.getReturnType() });
        mt.invoke(obj, ob);
        
        return ob;
    }
    
}
