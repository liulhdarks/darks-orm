package darks.orm.util;

import java.lang.reflect.Constructor;

import darks.orm.core.session.SessionContext;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

public class LogHelper
{
    
    public static void print(Object sp, Object obj)
    {
        if (SessionContext.getConfigure() != null)
        {
            if (!SessionContext.getConfigure().Logger().isShowLog())
                return;
        }
        System.out.print("[INFO][" + sp.getClass().getName() + "] " + obj);
    }
    
    public static void print(Object obj)
    {
        if (SessionContext.getConfigure() != null)
        {
            if (!SessionContext.getConfigure().Logger().isShowLog())
                return;
        }
        System.out.print("[INFO]" + obj);
    }
    
    @SuppressWarnings("unchecked")
    public static void print(Class cls, Object obj)
    {
        if (SessionContext.getConfigure() != null)
        {
            if (!SessionContext.getConfigure().Logger().isShowLog())
                return;
        }
        System.out.print("[INFO][" + cls.getName() + "] " + obj);
    }
    
    public static void println(Object obj)
    {
        if (SessionContext.getConfigure() != null)
        {
            if (!SessionContext.getConfigure().Logger().isShowLog())
                return;
        }
        System.out.println("[INFO]" + obj);
    }
    
    @SuppressWarnings("unchecked")
    public static void println(Class cls, Object obj)
    {
        if (SessionContext.getConfigure() != null)
        {
            if (!SessionContext.getConfigure().Logger().isShowLog())
                return;
        }
        System.out.println("[INFO][" + cls.getName() + "] " + obj);
    }
    
    public static void println(Object sp, Object obj)
    {
        if (SessionContext.getConfigure() != null)
        {
            if (!SessionContext.getConfigure().Logger().isShowLog())
                return;
        }
        System.out.println("[INFO][" + sp.getClass().getName() + "] " + obj);
    }
    
    public static <T extends Exception> void except(Object obj, String info, Class<T> clazz)
        throws T
    {
        Class<?> cobj = obj.getClass();
        Logger logger = LoggerFactory.getLogger(cobj);
        except(logger, info, clazz);
    }
    
    public static <T extends Exception> void except(Object obj, String info, Class<T> clazz, Throwable ex)
        throws T
    {
        Class<?> cobj = obj.getClass();
        Logger logger = LoggerFactory.getLogger(cobj);
        except(logger, info, clazz, ex);
    }
    
    public static <T extends Exception> void except(Logger logger, String info, Class<T> clazz)
        throws T
    {
        if (SessionContext.getConfigure() != null)
        {
            if (!SessionContext.getConfigure().Logger().isShowLog())
                return;
        }
        logger.error(info);
        Constructor<T> csrt = null;
        try
        {
            csrt = clazz.getConstructor(String.class);
        }
        catch (Exception e)
        {
            try
            {
                csrt = clazz.getDeclaredConstructor(String.class);
            }
            catch (Exception e1)
            {
                try
                {
                    throw clazz.newInstance();
                }
                catch (Exception e2)
                {
                    e2.printStackTrace();
                }
            }
        }
        try
        {
            if (csrt != null)
                throw csrt.newInstance(info);
            else
                throw new Exception(info);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static <T extends Exception> void except(Logger logger, String info, Class<T> clazz, Throwable ex)
        throws T
    {
        if (SessionContext.getConfigure() != null)
        {
            if (!SessionContext.getConfigure().Logger().isShowLog())
                return;
        }
        logger.error(info);
        Constructor<T> csrt = null;
        try
        {
            csrt = clazz.getConstructor(String.class, Throwable.class);
        }
        catch (Exception e)
        {
            try
            {
                csrt = clazz.getDeclaredConstructor(String.class, Throwable.class);
            }
            catch (Exception e1)
            {
                except(logger, info, clazz);
            }
        }
        try
        {
            if (csrt != null)
                throw csrt.newInstance(info, ex);
            else
                throw new Exception(info);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static <T extends Exception> void except(Logger logger, Class<T> clazz, Throwable ex)
        throws T
    {
        except(logger, ex.toString(), clazz, ex);
    }
    
    public static void SqlLog(String obj)
    {
        if (SessionContext.getConfigure() != null)
        {
            if (!SessionContext.getConfigure().Logger().isShowSql())
                return;
        }
        System.out.println("[SQL]" + obj.toUpperCase());
    }
}
