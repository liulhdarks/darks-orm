package darks.orm.core.aspect.jclass;

import java.lang.reflect.Method;

import darks.orm.app.SqlSession;
import darks.orm.app.QueryEnumType;
import darks.orm.core.aspect.UpdateAspectAdapter;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.SimpleAspectWrapper;
import darks.orm.exceptions.AspectException;
import darks.orm.util.ReflectHelper;

public class JavaClassUpdateAspect extends UpdateAspectAdapter
{
    
    private Class<?> aspectClass = null;
    
    private Object aepectObj = null;
    
    public JavaClassUpdateAspect()
    {
        
    }
    
    /**
     * 加载类，并初始化类实例
     * 
     * @param className 类全名
     * @return 类
     */
    private Class<?> getJavaClass(String className)
    {
        if (aspectClass != null && aepectObj != null)
            return aspectClass;
        try
        {
            aspectClass = Class.forName(className);
            aepectObj = ReflectHelper.newFastInstance(aspectClass);
            return aspectClass;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 反射调用AOP类方法
     * 
     * @param methodName 方法名称
     * @param className 类全名
     * @param dao BaseDAO
     * @param aspectData AOP xml配置文件数据
     * @param sqlMapAspectData 查询数据
     * @param queryEnumType 查询类型
     * @return 是否继续执行
     */
    private boolean invoke(String methodName, String className, SqlSession dao, AspectData aspectData,
        SimpleAspectWrapper simpleWrapper)
    {
        if (getJavaClass(className) == null || aepectObj == null)
            return false;
        Class<?>[] params = {SqlSession.class, AspectData.class, SimpleAspectWrapper.class, QueryEnumType.class};
        try
        {
            Method mt = aspectClass.getDeclaredMethod(methodName, params);
            if (mt == null)
                return false;
            Boolean ret = (Boolean)mt.invoke(aepectObj, dao, aspectData, simpleWrapper);
            return ret;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean afterInvoke(SqlSession session, AspectData aspectData, SimpleAspectWrapper simpleWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (aspectData == null)
            return true;
        String className = aspectData.getClassName();
        return invoke("afterInvoke", className, session, aspectData, simpleWrapper);
    }
    
    @Override
    public boolean beforeInvoke(SqlSession session, AspectData aspectData, SimpleAspectWrapper simpleWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (aspectData == null)
            return true;
        String className = aspectData.getClassName();
        return invoke("beforeInvoke", className, session, aspectData, simpleWrapper);
    }
    
}
