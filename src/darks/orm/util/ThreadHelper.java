/**
 * 类名:ThreadHelper.java
 * 作者:刘力华
 * 创建时间:2012-5-13
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadHelper
{
    
    private static final ExecutorService service = Executors.newCachedThreadPool();
    
    public static void shutdownNow()
    {
        shutdownNow(service);
    }
    
    public static void shutdownNow(ExecutorService exec)
    {
        if (exec != null)
        {
            List<Runnable> tasks = exec.shutdownNow();
            
            if (tasks.size() == 0)
            {
                System.out.println("Runnable tasks outlived thread pool executor service [" + ", tasks=" + tasks + ']');
            }
            
            try
            {
                exec.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e)
            {
                System.out.println("Got interrupted while waiting for executor service to stop.[" + e.toString() + "]");
            }
        }
    }
    
    public static void addThread(Runnable runnable)
    {
        service.execute(runnable);
    }
    
    public static void cleanThreadLocals(Thread thread)
        throws NoSuchFieldException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
    {
        if (thread == null)
            return;
        Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
        threadLocalsField.setAccessible(true);
        
        Class<?> threadLocalMapKlazz = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
        Field tableField = threadLocalMapKlazz.getDeclaredField("table");
        tableField.setAccessible(true);
        
        Object fieldLocal = threadLocalsField.get(thread);
        if (fieldLocal == null)
        {
            return;
        }
        Object table = tableField.get(fieldLocal);
        
        int threadLocalCount = Array.getLength(table);
        
        for (int i = 0; i < threadLocalCount; i++)
        {
            Object entry = Array.get(table, i);
            if (entry != null)
            {
                Field valueField = entry.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                Object value = valueField.get(entry);
                if (value != null)
                {
                    
                    if ("java.util.concurrent.ConcurrentLinkedQueue".equals(value.getClass().getName())
                        || "java.util.concurrent.ConcurrentHashMap".equals(value.getClass().getName()))
                    {
                        valueField.set(entry, null);
                    }
                }
                
            }
        }
    }
}
