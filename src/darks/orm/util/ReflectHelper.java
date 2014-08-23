
package darks.orm.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import darks.orm.core.data.FastClassData;
import darks.orm.core.factory.TransformFactory;
import darks.orm.exceptions.ClassReflectException;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;
import net.sf.cglib.reflect.FastMethod;

public final class ReflectHelper
{
    
    /**
     * 快速类数据缓存集合
     */
    private static final ConcurrentMap<Class<?>, FastClassData> fastClassMap =
        new ConcurrentHashMap<Class<?>, FastClassData>(32);
    
    /**
     * ResultSet映射get快速方法 get参数为列名
     */
    private static final ConcurrentMap<String, FastMethod> fastResultStringMethodMap =
        new ConcurrentHashMap<String, FastMethod>(32);
    
    /**
     * ResultSet映射get快速方法 get参数为列索引
     */
    private static final ConcurrentMap<String, FastMethod> fastResultIndexMethodMap =
        new ConcurrentHashMap<String, FastMethod>(32);
    
    private static FastClass fastClass;
    
    static
    {
        fastClass = FastClass.create(ResultSet.class);
        registerResultSetMethod();
    }
    
    // private static Logger log = LoggerFactory.getLogger(ReflectHelper.class);
    
    private ReflectHelper()
    {
        
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className, Object... params)
        throws ClassReflectException
    {
        
        Constructor<T> ctor = null;
        boolean set = false;
        try
        {
            Class<T> cls = (Class<T>)Class.forName(className);
            
            if (cls == null)
                return null;
            ctor = cls.getDeclaredConstructor();
            
            if (ctor == null)
            {
                return cls.newInstance();
            }
            
            if (!ctor.isAccessible())
            {
                ctor.setAccessible(true);
                
                set = true;
            }
            
            return ctor.newInstance(params);
        }
        catch (NoSuchMethodException e)
        {
            throw new ClassReflectException("Failed to find empty constructor for class: " + className, e);
        }
        catch (InstantiationException e)
        {
            throw new ClassReflectException("Failed to create new instance for class: " + className, e);
        }
        catch (IllegalAccessException e)
        {
            throw new ClassReflectException("Failed to create new instance for class: " + className, e);
        }
        catch (InvocationTargetException e)
        {
            throw new ClassReflectException("Failed to create new instance for class: " + className, e);
        }
        catch (ClassNotFoundException e)
        {
            throw new ClassReflectException("class " + className + "is not found", e);
        }
        finally
        {
            if (ctor != null && set)
            {
                ctor.setAccessible(false);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> clazz)
        throws ClassReflectException
    {
        if (clazz == null)
            return null;
        FastClassData data = parseFastClass(clazz);
        boolean set = false;
        
        Constructor<T> ctor = null;
        
        try
        {
            ctor = (Constructor<T>)data.getConstructor();
            
            if (ctor == null)
            {
                return clazz.newInstance();
            }
            
            if (!ctor.isAccessible())
            {
                ctor.setAccessible(true);
                
                set = true;
            }
            
            return ctor.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new ClassReflectException("Failed to create new instance for class: " + clazz, e);
        }
        catch (IllegalAccessException e)
        {
            throw new ClassReflectException("Failed to create new instance for class: " + clazz, e);
        }
        catch (InvocationTargetException e)
        {
            throw new ClassReflectException("Failed to create new instance for class: " + clazz, e);
        }
        finally
        {
            if (ctor != null && set)
            {
                ctor.setAccessible(false);
            }
        }
    }
    
    /**
     * 根据普通类快速创建对象
     * 
     * @param clazz 普通类
     * @return 对象实例
     * @throws ClassReflectException
     */
    @SuppressWarnings("unchecked")
    public static <T> T newFastInstance(Class<T> clazz)
        throws ClassReflectException
    {
        if (clazz == null)
            return null;
        FastClassData data = parseFastClass(clazz);
        FastConstructor fc = data.getFastConstructor();
        if (fc != null)
        {
            try
            {
                return (T)fc.newInstance();
            }
            catch (InvocationTargetException e)
            {
                throw new ClassReflectException("Failed to create new instance for class: " + clazz, e);
            }
        }
        FastClass fclass = data.getFastClass();
        if (fclass != null)
        {
            try
            {
                return (T)fclass.newInstance();
            }
            catch (InvocationTargetException e)
            {
                throw new ClassReflectException("Failed to create new instance for class: " + clazz, e);
            }
        }
        return newInstance(clazz);
    }
    
    /**
     * 创建快速类
     * 
     * @param clazz 原始类
     * @return
     */
    public static FastClass getFastClass(Class<?> clazz)
    {
        return FastClass.create(clazz);
    }
    
    /**
     * 创建快速方法
     * 
     * @param fasrClazz 快速类
     * @param method 原始方法
     * @return
     */
    public static FastMethod getFastMethod(FastClass fastClazz, Method method)
    {
        return fastClazz.getMethod(method);
    }
    
    /**
     * 创建快速方法
     * 
     * @param fasrClazz 快速类
     * @param method 原始方法
     * @return
     */
    public static FastMethod getFastMethod(FastClass fastClazz, String methodName, Class<?>... methodTypes)
        throws SecurityException, NoSuchMethodException
    {
        Class<?> clazz = fastClazz.getJavaClass();
        FastClassData data = parseFastClass(clazz);
        FastMethod fastMethod = data.getFastMethod(methodName);
        if (fastMethod == null)
        {
            Method method = clazz.getMethod(methodName, methodTypes);
            fastMethod = getFastMethod(data.getFastClass(), method);
            data.addFastMethod(method, fastMethod);
        }
        return fastMethod;
    }
    
    /**
     * 创建快速方法
     * 
     * @param fasrClazz 快速类
     * @param method 原始方法
     * @return
     */
    @SuppressWarnings("unchecked")
    public static FastMethod getFastMethod(Class clazz, String methodName, Class<?>... methodTypes)
        throws SecurityException, NoSuchMethodException
    {
        FastClassData data = parseFastClass(clazz);
        FastMethod fastMethod = data.getFastMethod(methodName);
        if (fastMethod == null)
        {
            Method method = clazz.getMethod(methodName, methodTypes);
            fastMethod = getFastMethod(data.getFastClass(), method);
            data.addFastMethod(method, fastMethod);
        }
        return fastMethod;
    }
    
    /**
     * 解析普通类生成快速类
     * 
     * @param clazz 普通类
     */
    public static FastClassData parseFastClass(Class<?> clazz)
    {
        
        FastClassData data = fastClassMap.get(clazz);
        if (data != null)
            return data;
        Constructor<?> constructor = null;
        FastConstructor fconstructor = null;
        data = new FastClassData();
        FastClass fastClazz = getFastClass(clazz);
        data.setOriginalClass(clazz);
        data.setFastClass(fastClazz);
        try
        {
            constructor = clazz.getConstructor();
            if (constructor != null)
            {
                fconstructor = fastClazz.getConstructor(constructor);
                data.setConstructor(constructor);
                data.setFastConstructor(fconstructor);
            }
        }
        catch (SecurityException e)
        {
            
        }
        catch (NoSuchMethodException e)
        {
            
        }
        fastClassMap.put(clazz, data);
        return data;
    }
    
    /**
     * 解析普通方法生成快速方法
     * 
     * @param clazz 普通类
     * @param method 普通方法
     */
    public static void parseFastMethod(Class<?> clazz, Method method)
    {
        FastClassData data = parseFastClass(clazz);
        FastMethod fastMethod = getFastMethod(data.getFastClass(), method);
        data.addFastMethod(method, fastMethod);
    }
    
    /**
     * 解析普通方法生成快速方法
     * 
     * @param clazz 普通类
     * @param methodName 普通方法名称
     * @param methodTypes 普通方法参数类型
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static FastMethod parseFastMethod(Class<?> clazz, String methodName, Class<?>... methodTypes)
        throws SecurityException, NoSuchMethodException
    {
        FastClassData data = parseFastClass(clazz);
        FastMethod fastMethod = data.getFastMethod(methodName);
        if (fastMethod == null)
        {
            Method method = clazz.getMethod(methodName, methodTypes);
            fastMethod = getFastMethod(data.getFastClass(), method);
            data.addFastMethod(method, fastMethod);
        }
        return fastMethod;
    }
    
    /**
     * 获取类所有属性(包括所有父类)
     * 
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field getField(Class<?> clazz, String fieldName)
        throws NoSuchFieldException
    {
        for (; clazz != Object.class; clazz = clazz.getSuperclass())
        {
            try
            {
                Field field = getAllField(clazz, fieldName);//clazz.getDeclaredField(fieldName);
                return field;
            }
            catch (Exception e)
            {
                continue;
            }
        }
        throw new NoSuchFieldException("con't find field '" + fieldName + "'");
    }
    
    /**
     * 设置属性字符串值
     * 
     * @param field 属性
     * @param obj 对象
     * @param value 值
     * @throws ClassReflectException
     */
    public static void setFieldString(Field field, Object obj, String value)
        throws ClassReflectException
    {
        Class<?> typeClazz = field.getType();
        Object val = null;
        if (typeClazz.equals(String.class))
        {
            val = value;
        }
        else if ((value == null || "".equals(value)))
        {
            if (!typeClazz.equals(String.class))
                val = 0;
        }
        else
        {
            if (typeClazz.equals(Integer.class) || typeClazz.equals(int.class))
            {
                val = Integer.parseInt(value);
            }
            else if (typeClazz.equals(Short.class) || typeClazz.equals(short.class))
            {
                val = Short.parseShort(value);
            }
            else if (typeClazz.equals(Float.class) || typeClazz.equals(float.class))
            {
                val = Float.parseFloat(value);
            }
            else if (typeClazz.equals(Double.class) || typeClazz.equals(double.class))
            {
                val = Double.parseDouble(value);
            }
            else if (typeClazz.equals(Long.class) || typeClazz.equals(long.class))
            {
                val = Double.parseDouble(value);
            }
            else if (typeClazz.equals(Byte.class) || typeClazz.equals(byte.class))
            {
                val = Byte.parseByte(value);
            }
            else if (typeClazz.equals(Boolean.class) || typeClazz.equals(boolean.class))
            {
                val = Boolean.parseBoolean(value);
            }
        }
        setFieldValue(field, obj, val);
    }
    
    /**
     * 设置属性值
     * 
     * @param field 属性
     * @param obj 对象
     * @param value 值
     * @throws ClassReflectException
     */
    public static void setFieldValue(Field field, Object obj, Object value)
        throws ClassReflectException
    {
        try
        {
            boolean acc = field.isAccessible();
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(acc);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ClassReflectException("set field '" + field.getName() + "' is error", e);
        }
    }
    
    /**
     * 注册ResultSet setter,getter方法
     */
    private static void registerResultSetMethod()
    {
        Class<ResultSet> clazz = ResultSet.class;
        Method[] mts = clazz.getDeclaredMethods();
        for (Method mt : mts)
        {
            String name = mt.getName();
            if (name.startsWith("get"))
            {
                FastMethod fastMethod = fastClass.getMethod(mt);
                Class<?>[] types = fastMethod.getParameterTypes();
                if (types.length == 1)
                {
                    if (types[0].equals(String.class))
                        fastResultStringMethodMap.put(name, fastMethod);
                    if (types[0].equals(int.class))
                        fastResultIndexMethodMap.put(name, fastMethod);
                }
            }
        }
    }
    
    /**
     * 获得ResultSet映射快速方法 参数为列名
     * 
     * @param methodName 方法名
     * @return 快速方法
     */
    public static FastMethod getResultSetStringFastMethod(String methodName)
    {
        return fastResultStringMethodMap.get(methodName);
    }
    
    /**
     * 获得ResultSet映射快速方法 参数为列索引
     * 
     * @param methodName 方法名
     * @return 快速方法
     */
    public static FastMethod getResultSetIndexFastMethod(String methodName)
    {
        return fastResultIndexMethodMap.get(methodName);
    }
    
    /**
     * 判断是否是接口类
     * 
     * @param clazz 类
     * @return
     */
    public static boolean isInterfaceClass(Class<?> clazz)
    {
        return clazz.isInterface();
    }
    
    /**
     * 反射结果集指定索引列值
     * 
     * @param rs 结果集
     * @param javaType 返回JAVA类型
     * @param colnumIndex 列索引
     * @return 值
     * @throws Exception
     */
    public static final Object getResultSetValue(ResultSet rs, Class<?> javaType, int colnumIndex)
        throws ClassReflectException
    {
        String jdbcmt = TransformFactory.getInstance().getJdbcResultMethod(javaType);
        FastMethod method = ReflectHelper.getResultSetIndexFastMethod(jdbcmt);
        if (method == null)
        {
            Class<?> rsClass = rs.getClass();
            Method m = null;
            try
            {
                m = rsClass.getMethod(jdbcmt, new Class[] {int.class});
                return m.invoke(rs, colnumIndex);
            }
            catch (NoSuchMethodException e)
            {
                throw new ClassReflectException("ReflectHelper::getResultSetValue happen NoSuchMethodException "
                    + e.toString(), e);
            }
            catch (Exception e)
            {
                throw new ClassReflectException("ReflectHelper::getResultSetValue happen Exception " + e.toString(), e);
            }
            
        }
        try
        {
            return method.invoke(rs, new Object[] {colnumIndex});
        }
        catch (InvocationTargetException e)
        {
            throw new ClassReflectException("ReflectHelper::getResultSetValue happen InvocationTargetException "
                + e.toString(), e);
        }
    }
    
    /**
     * 反射结果集指定列名值
     * 
     * @param rs 结果集
     * @param javaType 返回JAVA类型
     * @param colnumName 列名
     * @return 值
     * @throws Exception
     */
    public static final Object getResultSetValue(ResultSet rs, Class<?> javaType, String colnumName)
        throws Exception
    {
        String jdbcmt = TransformFactory.getInstance().getJdbcResultMethod(javaType);
        FastMethod method = ReflectHelper.getResultSetStringFastMethod(jdbcmt);
        if (method == null)
        {
            Class<?> rsClass = rs.getClass();
            Method m = rsClass.getMethod(jdbcmt, new Class[] {String.class});
            return m.invoke(rs, colnumName);
        }
        return method.invoke(rs, new Object[] {colnumName});
    }
    
    public static Field getAllField(Class<?> clazz, String fieldName)
    {
        if (Object.class.equals(clazz))
            throw new ClassReflectException("'" + fieldName + "' does not exists in " + clazz);
        Field result = null;
        try
        {
            result = clazz.getDeclaredField(fieldName);
        }
        catch (Exception e)
        {
            result = getAllField(clazz.getSuperclass(), fieldName);
        }
        return result;
    }
    
    public static Field[] getAllFields(Class<?> clazz)
    {
        List<Field> fields = new LinkedList<Field>();
        
        getAllFields(fields, clazz);
        
        Field[] result = new Field[fields.size()];
        fields.toArray(result);
        return result;
    }
    
    private static void getAllFields(List<Field> fields, Class<?> clazz)
    {
        if (Object.class.equals(clazz))
            return;
        
        for (Field field : clazz.getDeclaredFields())
        {
            fields.add(field);
        }
        getAllFields(fields, clazz.getSuperclass());
    }
    
    public static Method getAllMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes)
    {
        if (Object.class.equals(clazz))
            throw new ClassReflectException("'" + methodName + "' does not exists in " + clazz);
        Method result = null;
        try
        {
            result = clazz.getDeclaredMethod(methodName, parameterTypes);
        }
        catch (Exception e)
        {
            result = getAllMethod(clazz.getSuperclass(), methodName);
        }
        return result;
    }
    
    public static Method[] getAllMethods(Class<?> clazz)
    {
        List<Method> methods = new LinkedList<Method>();
        
        getAllMethods(methods, clazz);
        
        Method[] result = new Method[methods.size()];
        methods.toArray(result);
        return result;
    }
    
    private static void getAllMethods(List<Method> methods, Class<?> clazz)
    {
        if (Object.class.equals(clazz))
            return;
        
        for (Method method : clazz.getDeclaredMethods())
        {
            methods.add(method);
        }
        getAllMethods(methods, clazz.getSuperclass());
    }
}
