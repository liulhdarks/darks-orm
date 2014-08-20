/**
 * 类名:FastClassData.java
 * 作者:刘力华
 * 创建时间:2012-5-3
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.core.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;
import net.sf.cglib.reflect.FastMethod;

public class FastClassData
{
    
    /**
     * 原始类 键值
     */
    private Class<?> clazz;
    
    /**
     * 快速类
     */
    private FastClass fastClass;
    
    /**
     * 原始构造函数
     */
    private Constructor<?> constructor;
    
    /**
     * 快速构造函数
     */
    private FastConstructor fastConstructor;
    
    /**
     * 快速方法集合
     */
    private Map<String, FastMethod> fastMethodMap = new ConcurrentHashMap<String, FastMethod>();
    
    public FastClassData()
    {
        
    }
    
    /**
     * 获得原始类
     * 
     * @return
     */
    public Class<?> getOriginalClass()
    {
        return clazz;
    }
    
    public void setOriginalClass(Class<?> clazz)
    {
        this.clazz = clazz;
    }
    
    public FastClass getFastClass()
    {
        return fastClass;
    }
    
    public void setFastClass(FastClass fastClass)
    {
        this.fastClass = fastClass;
    }
    
    public FastConstructor getFastConstructor()
    {
        return fastConstructor;
    }
    
    public void setFastConstructor(FastConstructor fastConstructor)
    {
        this.fastConstructor = fastConstructor;
    }
    
    public Constructor<?> getConstructor()
    {
        return constructor;
    }
    
    public void setConstructor(Constructor<?> constructor)
    {
        this.constructor = constructor;
    }
    
    /**
     * 添加快速方法
     * 
     * @param method 原始方法
     * @param fastMethod 快速方法
     */
    public void addFastMethod(Method method, FastMethod fastMethod)
    {
        fastMethodMap.put(method.getName(), fastMethod);
    }
    
    /**
     * 添加快速方法
     * 
     * @param method 原始方法
     * @param fastMethod 快速方法
     */
    public FastMethod getFastMethod(String methodName)
    {
        return fastMethodMap.get(methodName);
    }
}
