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

package darks.orm.core.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.sf.cglib.beans.BeanGenerator;
import darks.orm.annotation.Column;
import darks.orm.annotation.Entity;
import darks.orm.annotation.Id;
import darks.orm.annotation.ManyToOne;
import darks.orm.annotation.OneToMany;
import darks.orm.annotation.OneToOne;
import darks.orm.annotation.Param;
import darks.orm.annotation.Temporary;
import darks.orm.annotation.sqlmap.Query;
import darks.orm.annotation.sqlmap.Update;
import darks.orm.core.data.EntityData;
import darks.orm.core.data.FieldData;
import darks.orm.core.data.FieldData.FieldFlag;
import darks.orm.core.data.MethodData;
import darks.orm.core.data.MethodData.MethodEnumType;
import darks.orm.core.data.PrimaryKeyData;
import darks.orm.core.data.xml.InterfaceMethodData;
import darks.orm.exceptions.AssistException;
import darks.orm.exceptions.ClassReflectException;
import darks.orm.exceptions.SessionException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.ByteHelper;
import darks.orm.util.DataTypeHelper;
import darks.orm.util.ReflectHelper;
import darks.orm.util.StringHelper;

/**
 * 
 * 
 * <p>
 * <h1>ClassFactory.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public class ClassFactory
{

	private static final Logger log = LoggerFactory.getLogger(ClassFactory.class);

	private static final ConcurrentMap<String, EntityData> mapEntitys = new ConcurrentHashMap<String, EntityData>();

	private static final ConcurrentMap<String, MethodData> mapMethods = new ConcurrentHashMap<String, MethodData>();

	private static final ConcurrentMap<String, InterfaceMethodData> mapIFaceMethod = new ConcurrentHashMap<String, InterfaceMethodData>();

	/**
	 * 获得实体类
	 * 
	 * @param className
	 *            类全名
	 * @return
	 */
	public static Class<?> getClass(String className)
	{
		EntityData data = mapEntitys.get(className);
		if (data == null)
			return null;
		return data.getClassProxy();
	}

	public static Class<?> getResultSetClass()
	{
		return ResultSet.class;
	}

	/**
	 * 获得实体数据
	 * 
	 * @param className
	 *            类全名
	 * @return
	 */
	public static EntityData getEntity(String className)
	{
		return mapEntitys.get(className);
	}

	/**
	 * 获得实体方法
	 * 
	 * @param methodGenericName
	 *            方法全名
	 * @return
	 */
	public static MethodData getMethod(String methodGenericName)
	{
		return mapMethods.get(methodGenericName);
	}

	/**
	 * 初始化实体类
	 * 
	 * @param <T>
	 *            实体类范型
	 * @param cs
	 *            实体类集合
	 * @throws Exception
	 */
	public static <T> void initClass(List<Class<T>> cs) throws Exception
	{
		for (Class<T> c : cs)
		{
			parseClass(c);
		}
	}

	/**
	 * 通过实体类全名列表初始化实体类
	 * 
	 * @param entitys
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public static void initEntity(Collection<Class<?>> entitys) throws ClassNotFoundException
	{
		if (entitys.size() == 0)
			return;
		for (Class<?> clazz : entitys)
		{
			if (clazz == null)
				continue;
			parseClass(clazz);
		}
	}

	/**
	 * 过滤代理类，得到其对应的实体类
	 * 
	 * @param className
	 *            代理类名称
	 * @return 实体类全名
	 * @throws Exception
	 */
	public static String filterClassName(String className)
	{
		if (className.indexOf("$") < 0)
			return className;
		className = className.substring(0, className.indexOf("$"));
		return className;
	}

	public static InterfaceMethodData getInterfaceClass(String id)
	{
		return mapIFaceMethod.get(id);
	}

	public static <T> InterfaceMethodData parseInterfaceClass(String id, Method method)
	{
		InterfaceMethodData data = getInterfaceClass(id);
		if (data != null)
		{
			return data;
		}
		synchronized (method)
		{
			data = getInterfaceClass(id);
			if (data != null)
			{
				return data;
			}
			Query query = method.getAnnotation(Query.class);
			Update update = method.getAnnotation(Update.class);
			data = new InterfaceMethodData(query, update, method);
			mapIFaceMethod.put(id, data);
			return data;
		}
	}

	/**
	 * 压入类，分析类结构
	 * 
	 * @param <T>
	 *            实体类范型
	 * @param cls
	 *            实体类
	 * @return 类字段集合
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public static <T> EntityData parseClass(Class<T> cls) throws SessionException
	{
		if (DataTypeHelper.checkClassIsBasicDataType(cls))
			return null;
		String className = cls.getName();
		String classSrc = filterClassName(className);
		className = classSrc;
		Class<?> c = null;
		try
		{
			c = Class.forName(classSrc);
		}
		catch (ClassNotFoundException e)
		{
			throw new SessionException("ClassFactory::parseClass ClassNotFoundException "
					+ e.toString(), e);
		}
		EntityData entityData = mapEntitys.get(className);
		// if(!mapEntitys.containsKey(className)){
		if (entityData == null)
		{
			synchronized (cls)
			{
				entityData = mapEntitys.get(className);
				if (entityData != null)
					return entityData;
				if (!cls.isAnnotationPresent(Entity.class))
				{
					log.error(cls + " does not have annotation");
					throw new ClassReflectException(cls + " does not have annotation");
				}
				Entity entity = (Entity) cls.getAnnotation(Entity.class);
				entityData = new EntityData();
				mapEntitys.put(className, entityData);
				entityData.setClassName(className);
				entityData.setClassOrignal(c);
				entityData.setSchema(entity.schema());
				entityData.setTableName(entity.value());
				entityData.setSerializable(ByteHelper.isSerializable(cls));
				ReflectHelper.parseFastClass(c);
				BeanGenerator g = BeanGeneratorFactory.generateProxyBean(c);

				Field[] fields = ReflectHelper.getAllFields(c);// c.getDeclaredFields();
				Method[] methods = ReflectHelper.getAllMethods(c);// c.getDeclaredMethods();

				// ----FOREACH FIELDS START
				for (Field f : fields)
				{
					addEntityField(entityData, g, f);
				}
				// ----FOREACH FIELDS END
				// ----FOREACH METHODS START
				for (Method method : methods)
				{
					ReflectHelper.parseFastMethod(c, method);
					addEntityMethod(entityData, g, method);
				}
				// ----FOREACH METHODS END
				Class<?> proxy = (Class<?>) g.createClass();
				entityData.setClassProxy(proxy);

			}
			// return entityData;
		}
		// else{
		// return mapEntitys.get(className);
		// }
		return entityData;
	}

	/**
	 * 添加实体属性
	 * 
	 * @param entityData
	 *            实体数据
	 * @param g
	 *            BeanGenerator实例
	 * @param f
	 *            反射属性
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private static void addEntityField(EntityData entityData, BeanGenerator g, Field f)
			throws SessionException
	{
		// 是否是临时字段
		if (Modifier.isFinal(f.getModifiers()))
			return;
		Temporary tmp = (Temporary) f.getAnnotation(Temporary.class);
		if (tmp != null)
			return;

		FieldData fieldData = new FieldData();
		fieldData.setFieldFlag(FieldFlag.Normal);
		Class<?> ftype = f.getType();
		String type = ftype.getSimpleName();
		type = StringHelper.upperHeadWord(type);
		fieldData.setFieldType(type);
		fieldData.setFieldClass(ftype);
		fieldData.setField(f);
		String name = f.getName();
		fieldData.setFieldName(name);
		if (type.indexOf("List") >= 0 || type.indexOf("Set") >= 0)
		{
			fieldData.setFieldFlag(FieldFlag.Collection);
			fieldData.setColumnName(name);
			fieldData.setInsertable(false);
			fieldData.setNullable(false);
			fieldData.setQueryable(false);
			fieldData.setUpdatable(false);
			entityData.addField(fieldData);
			return;
		}

		Column cl = (Column) f.getAnnotation(Column.class);
		if (cl != null)
		{
			name = cl.value().toUpperCase();
			fieldData.setInsertable(cl.insertable());
			fieldData.setNullable(cl.nullable());
			fieldData.setQueryable(cl.queryable());
			fieldData.setUpdatable(cl.updatable());
		}
		fieldData.setColumnName(name);

		// 是否是外键实体字段
		Entity fkEntity = (Entity) f.getType().getAnnotation(Entity.class);
		if (fkEntity != null)
		{
			fieldData.setFieldFlag(FieldFlag.FkEntity);
			String mn = StringHelper.upperHeadWord(fieldData.getFieldName());
			fieldData.setFkField("fk_" + mn);
			fieldData.setFkSetMethod("setFk_" + mn);
			fieldData.setFkGetMethod("getFk_" + mn);
			fieldData.setFkClass(f.getType());
			EntityData fdata = parseClass(f.getType());
			if (!fdata.isSerializable())
				entityData.setSerializable(false);
			fieldData.setFkData(fdata);
			FieldData fpkdata = fdata.getPkField();
			if (fpkdata == null)
			{
				throw new AssistException(f.getType() + " Lack primary key field.");
			}
			g.addProperty(fieldData.getFkField(), fpkdata.getFieldClass());
		}

		// 是否有主键注解
		Id pkid = (Id) f.getAnnotation(Id.class);
		if (pkid != null)
		{ // 是否为主键
			fieldData.setPrimaryKey(true);
			PrimaryKeyData pkdata = new PrimaryKeyData(pkid.type(), pkid.seq(), pkid.feedBackKey(),
					pkid.select());
			fieldData.setPkData(pkdata);
			entityData.addPkField(fieldData); // 向实体信息添加列信息
		}
		else
		{
			entityData.addField(fieldData); // 向实体信息添加列信息
		}
	}

	/**
	 * 添加实体方法
	 * 
	 * @param entityData
	 *            实体数据
	 * @param g
	 *            BeanGenerator实例
	 * @param method
	 *            反射方法
	 * @throws Exception
	 */
	private static void addEntityMethod(EntityData entityData, BeanGenerator g, Method method)
	{
		MethodData methodData = new MethodData();
		methodData.setMethod(method);
		methodData.setMethodName(method.toGenericString());
		methodData.setMethodSimpleName(method.getName());
		for (int i = 0; i < method.getParameterTypes().length; i++)
		{
			try
			{
				Param param = (Param) method.getParameterAnnotations()[i][0];
				if (param == null)
					continue;
				methodData.addArgument(i + 1, param.value());
			}
			catch (Exception e)
			{
			}
		}
		methodData.setMethodEnumType(MethodEnumType.None);
		if (method.getAnnotation(darks.orm.annotation.Query.class) != null)
		{
			methodData.setMethodEnumType(MethodEnumType.Query);
			entityData.setUseProxy(true);
		}
		else
		{
			if (method.getAnnotation(OneToMany.class) != null)
			{
				methodData.setMethodEnumType(MethodEnumType.OneToMany);
				entityData.setUseProxy(true);
			}
			else
			{
				if (method.getAnnotation(ManyToOne.class) != null)
				{
					methodData.setMethodEnumType(MethodEnumType.ManyToOne);
					entityData.setUseProxy(true);
				}
				else
				{
					if (method.getAnnotation(OneToOne.class) != null)
					{
						methodData.setMethodEnumType(MethodEnumType.OneToOne);
						entityData.setUseProxy(true);
					}
				}
			}
		}
		entityData.addMethod(methodData);
		mapMethods.put(methodData.getMethodName(), methodData);
	}

	/**
	 * 获得主键名称
	 * 
	 * @param c
	 *            类
	 * @return 主键名称
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public static <T> String getPrimaryKeyName(Class<T> c) throws SessionException
	{
		FieldData data = getPrimaryKey(c);
		if (data != null)
		{
			return data.getColumnName();
		}
		return null;
	}

	/**
	 * 获得主键值
	 * 
	 * @param c
	 *            类
	 * @return 主键名称
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	public static <T> Object getPrimaryKeyValue(Class<?> c, T obj) throws SessionException
	{
		FieldData data = getPrimaryKey(c);
		if (data != null)
		{
			return data.getValue(obj);
		}
		return null;
	}

	/**
	 * 获得主键值
	 * 
	 * @param c
	 *            类
	 * @return 主键名称
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public static FieldData getPrimaryKey(Class<?> c) throws SessionException
	{
		boolean flag = c.isAnnotationPresent(Entity.class);
		if (!flag)
			return null;
		EntityData entityData = null;
		if (mapEntitys.containsKey(c.getName()))
		{
			entityData = mapEntitys.get(c.getName());
		}
		else
		{
			entityData = parseClass(c);
		}
		if (entityData != null)
		{
			return entityData.getPkField();
		}
		return null;
	}

	/**
	 * 获得字段字符串
	 * 
	 * @param c
	 *            类
	 * @return 字段连接字符串
	 * @throws ClassNotFoundException
	 */
	public static <T> String getFieldNames(Class<T> c) throws SessionException
	{

		if (mapEntitys.containsKey(c.getName()))
		{
			EntityData entityData = mapEntitys.get(c.getName());
			return entityData.getFieldString();
		}
		EntityData entityData = parseClass(c);
		return entityData.getFieldString();
	}

	/**
	 * 获得表名
	 * 
	 * @param c
	 *            类
	 * @return 表名字符串
	 * @throws ClassNotFoundException
	 */
	public static <T> String getTableName(Class<T> c) throws SessionException
	{
		if (c == null)
			return null;
		boolean flag = c.isAnnotationPresent(Entity.class);
		if (!flag)
			return null;
		if (mapEntitys.containsKey(c.getName()))
		{
			EntityData entityData = mapEntitys.get(c.getName());
			return entityData.getTableName();
		}
		EntityData entityData = parseClass(c);
		return entityData.getTableName();
	}

}