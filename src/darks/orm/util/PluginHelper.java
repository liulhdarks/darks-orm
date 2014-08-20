/**
 * 类名:PluginHelper.java
 * 作者:刘力华
 * 创建时间:2012-5-13
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.util;

public class PluginHelper
{
    
    public static boolean isRegJythonPlugin()
    {
        try
        {
            Class<?> cls = Class.forName("org.python.util.PythonInterpreter");
            if (cls == null)
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    public static boolean isRegSpringRequestPlugin()
    {
        try
        {
            Class<?> cls = Class.forName("org.springframework.web.context.request.RequestContextListener");
            if (cls == null)
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    public static boolean isRegSpringContextPlugin()
    {
        try
        {
            Class<?> cls = Class.forName("org.springframework.context.ApplicationContext");
            if (cls == null)
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
}
