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

package darks.orm.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastConstructor;
import net.sf.cglib.reflect.FastMethod;
import darks.orm.core.data.FastClassData;
import darks.orm.core.factory.TransformFactory;
import darks.orm.exceptions.ClassReflectException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

public final class ReflectHelper
{

	private static Logger log = LoggerFactory.getLogger(ReflectHelper.class);
	
	/**
	 * 快速类数据缓存集合
	 */
	private static final ConcurrentMap<Class<?>, FastClassData> fastClassMap = new ConcurrentHashMap<Class<?>, FastClassData>(
			32);

	/**
	 * ResultSet映射get快速方法 get参数为列名
	 */
	private static final ConcurrentMap<String, FastMethod> fastResultStringMethodMap = new ConcurrentHashMap<String, FastMethod>(
			32);

	/**
	 * ResultSet映射get快速方法 get参数为列索引
	 */
	private static final ConcurrentMap<String, FastMethod> fastResultIndexMethodMap = new ConcurrentHashMap<String, FastMethod>(
			32);

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
			Class<T> cls = (Class<T>) Class.forName(className);

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
			throw new ClassReflectException("Failed to find empty constructor for class: "
					+ className, e);
		}
		catch (Exception e)
		{
			throw new ClassReflectException(
					"Failed to create new instance for class: " + className, e);
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
	public static <T> T newInstance(Class<T> clazz) throws ClassReflectException
	{
		if (clazz == null)
			return null;
		FastClassData data = parseFastClass(clazz);
		boolean set = false;

		Constructor<T> ctor = null;

		try
		{
			ctor = (Constructor<T>) data.getConstructor();

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
		catch (Exception e)
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
	public static <T> T newFastInstance(Class<T> clazz) throws ClassReflectException
	{
		if (clazz == null)
			return null;
		FastClassData data = parseFastClass(clazz);
		FastConstructor fc = data.getFastConstructor();
		if (fc != null)
		{
			try
			{
				return (T) fc.newInstance();
			}
			catch (InvocationTargetException e)
			{
				throw new ClassReflectException(
						"Failed to create new instance for class: " + clazz, e);
			}
		}
		FastClass fclass = data.getFastClass();
		if (fclass != null)
		{
			try
			{
				return (T) fclass.newInstance();
			}
			catch (InvocationTargetException e)
			{
				throw new ClassReflectException(
						"Failed to create new instance for class: " + clazz, e);
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
	public static FastMethod getFastMethod(FastClass fastClazz, String methodName,
			Class<?>... methodTypes) throws SecurityException, NoSuchMethodException
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
	public static FastMethod getFastMethod(Class<?> clazz, String methodName,
			Class<?>... methodTypes) throws SecurityException, NoSuchMethodException
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
	public static FastMethod parseFastMethod(Class<?> clazz, String methodName,
			Class<?>... methodTypes) throws SecurityException, NoSuchMethodException
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
	public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException
	{
		for (; clazz != Object.class; clazz = clazz.getSuperclass())
		{
			try
			{
				Field field = getAllField(clazz, fieldName);// clazz.getDeclaredField(fieldName);
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
				m = rsClass.getMethod(jdbcmt, new Class[] { int.class });
				return m.invoke(rs, colnumIndex);
			}
			catch (NoSuchMethodException e)
			{
				throw new ClassReflectException(
						"ReflectHelper::getResultSetValue happen NoSuchMethodException "
								+ e.toString(), e);
			}
			catch (Exception e)
			{
				throw new ClassReflectException(
						"ReflectHelper::getResultSetValue happen Exception " + e.toString(), e);
			}

		}
		try
		{
			return method.invoke(rs, new Object[] { colnumIndex });
		}
		catch (InvocationTargetException e)
		{
			throw new ClassReflectException(
					"ReflectHelper::getResultSetValue happen InvocationTargetException "
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
			Method m = rsClass.getMethod(jdbcmt, new Class[] { String.class });
			return m.invoke(rs, colnumName);
		}
		return method.invoke(rs, new Object[] { colnumName });
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

	public static Object getFieldValue(Field field, Object target)
	{
		boolean isAccess = field.isAccessible();
		if (!isAccess)
			field.setAccessible(true);
		try
		{
			return field.get(target);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if (!isAccess)
				field.setAccessible(isAccess);
		}
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

	public static List<Class<?>> scanPackageClasses(String packageName, boolean recursive)
	{
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String packageDirName = packageName.replace('.', '/');
		Enumeration<URL> dirs;
		try
		{
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements())
			{
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				if ("file".equals(protocol))
				{
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				}
				else if ("jar".equals(protocol))
				{
					try
					{
						JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
						scanJarPackageClass(classes, packageDirName, packageName, jar, recursive);
					}
					catch (IOException e)
					{
						log.error("Fail to scan jar package classes.Cause " + e.getMessage(), e);
					}
				}
			}
		}
		catch (IOException e)
		{
			log.error("Fail to scan package classes.Cause " + e.getMessage(), e);
		}
		return classes;
	}

	private static void scanJarPackageClass(List<Class<?>> classes, String packageDirName,
			String packageName, JarFile jar, boolean recursive)
	{
		// 从此jar包 得到一个枚举类
		Enumeration<JarEntry> entries = jar.entries();
		// 同样的进行循环迭代
		while (entries.hasMoreElements())
		{
			// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			// 如果是以/开头的
			if (name.charAt(0) == '/')
			{
				// 获取后面的字符串
				name = name.substring(1);
			}
			// 如果前半部分和定义的包名相同
			if (name.startsWith(packageDirName))
			{
				int idx = name.lastIndexOf('/');
				// 如果以"/"结尾 是一个包
				if (idx != -1)
				{
					// 获取包名 把"/"替换成"."
					packageName = name.substring(0, idx).replace('/', '.');
				}
				// 如果可以迭代下去 并且是一个包
				if ((idx != -1) || recursive)
				{
					// 如果是一个.class文件 而且不是目录
					if (name.endsWith(".class") && !entry.isDirectory())
					{
						// 去掉后面的".class" 获取真正的类名
						String className = name.substring(packageName.length() + 1,
								name.length() - 6);
						try
						{
							// 添加到classes
							classes.add(Class.forName(packageName + '.' + className));
						}
						catch (ClassNotFoundException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath,
			final boolean recursive, List<Class<?>> classes)
	{
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory())
		{
			return;
		}
		File[] dirfiles = dir.listFiles(new FileFilter()
		{
			public boolean accept(File file)
			{
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		for (File file : dirfiles)
		{
			if (file.isDirectory())
			{
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive, classes);
			}
			else
			{
				String className = file.getName().substring(0, file.getName().length() - 6);
				try
				{
					classes.add(Class.forName(packageName + '.' + className));
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
