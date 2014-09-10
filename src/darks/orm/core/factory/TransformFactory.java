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

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.sf.cglib.reflect.FastMethod;

import darks.orm.annotation.Entity;
import darks.orm.app.SqlSession;
import darks.orm.core.data.EntityData;
import darks.orm.core.data.FieldData;
import darks.orm.core.data.FieldData.FieldFlag;
import darks.orm.core.session.SessionContext;
import darks.orm.exceptions.TransformException;
import darks.orm.util.DataTypeHelper;
import darks.orm.util.ReflectHelper;

/**
 * 
 * 
 * <p>
 * <h1>TransformFactory.java</h1>
 * <p>
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012 
 * @since JDK1.5
 */
public class TransformFactory
{
    
    public static final String SQL_COUNT_ALIAS = "SQL_COUNT_ALIAS";
    
    public static final int SQL_TRANSMAP_INIT_SIZE = 100;
    
    public static final int SQL_JDBCRET_INIT_SIZE = 50;
    
    public static final int SQL_STRCLASS_INIT_SIZE = 50;
    
    public static final int SQL_COLUMN_INIT_SIZE = 128;
    
    private static final String ENTITY_CASCADE_DIV = "_";
    
    private transient static final ConcurrentMap<String, String> sqlTranMap = new ConcurrentHashMap<String, String>(
        SQL_TRANSMAP_INIT_SIZE);
    
    private transient static final ConcurrentMap<Class<?>, String> jdbcResultMethodMap =
        new ConcurrentHashMap<Class<?>, String>(SQL_JDBCRET_INIT_SIZE);
    
    private transient static final ConcurrentMap<String, Class<?>> stringClassMap =
        new ConcurrentHashMap<String, Class<?>>(SQL_STRCLASS_INIT_SIZE);
    
    private transient static final ConcurrentMap<String, Map<String, Integer>> colsMap =
        new ConcurrentHashMap<String, Map<String, Integer>>(SQL_COLUMN_INIT_SIZE);
    
    private static volatile TransformFactory instance = null;
    
    private static final Lock lock = new ReentrantLock();
    
    private TransformFactory()
    {
        registerJdbcResultMethodMap();
        registerStringClassMap();
    }
    
    /**
     * 注册ResultSet的类型获取映射
     */
    public void registerJdbcResultMethodMap()
    {
        registerJdbcResultMethod(Integer.class, "getInt");
        registerJdbcResultMethod(int.class, "getInt");
        registerJdbcResultMethod(String.class, "getString");
        registerJdbcResultMethod(Short.class, "getShort");
        registerJdbcResultMethod(short.class, "getShort");
        registerJdbcResultMethod(Long.class, "getLong");
        registerJdbcResultMethod(long.class, "getLong");
        registerJdbcResultMethod(Double.class, "getDouble");
        registerJdbcResultMethod(double.class, "getDouble");
        registerJdbcResultMethod(Date.class, "getDate");
        registerJdbcResultMethod(Time.class, "getTime");
        registerJdbcResultMethod(Timestamp.class, "getTimestamp");
        registerJdbcResultMethod(Blob.class, "getBlob");
        registerJdbcResultMethod(Clob.class, "getClob");
        registerJdbcResultMethod(NClob.class, "getNClob");
        registerJdbcResultMethod(Array.class, "getArray");
        registerJdbcResultMethod(BigDecimal.class, "getBigDecimal");
        registerJdbcResultMethod(Boolean.class, "getBoolean");
        registerJdbcResultMethod(boolean.class, "getBoolean");
        registerJdbcResultMethod(Byte.class, "getByte");
        registerJdbcResultMethod(byte.class, "getByte");
        registerJdbcResultMethod(byte[].class, "getBytes");
        registerJdbcResultMethod(float.class, "getFloat");
        registerJdbcResultMethod(Float.class, "getFloat");
        registerJdbcResultMethod(URL.class, "getURL");
        registerJdbcResultMethod(SQLXML.class, "getSQLXML");
        registerJdbcResultMethod(Ref.class, "getRef");
        registerJdbcResultMethod(RowId.class, "getRowId");
    }
    
    public void registerStringClassMap()
    {
        registerStringClass(Integer.class, "Integer");
        registerStringClass(int.class, "int");
        registerStringClass(String.class, "String");
        registerStringClass(Short.class, "Short");
        registerStringClass(short.class, "short");
        registerStringClass(Long.class, "Long");
        registerStringClass(long.class, "long");
        registerStringClass(Double.class, "Double");
        registerStringClass(double.class, "double");
        registerStringClass(Boolean.class, "Boolean");
        registerStringClass(boolean.class, "boolean");
        registerStringClass(Byte.class, "Byte");
        registerStringClass(byte.class, "byte");
        registerStringClass(byte[].class, "bytes");
        registerStringClass(float.class, "float");
        registerStringClass(Float.class, "Float");
    }
    
    /**
     * 通过字符串获得实体数据类型
     * 
     * @param strClass 类字符串
     * @return 类
     * @throws ClassNotFoundException
     * @throws Exception
     */
    public Class<?> stringToEntityClass(String strClass)
        throws ClassNotFoundException
    {
        Class<?> ret = stringClassMap.get(strClass);
        if (ret == null)
        {
            ret = SessionContext.getConfigure().getEntityConfig().getEntity(strClass);
            if (ret != null)
                return ret;
            return SessionContext.getConfigure().getEntityConfig().addEntityConfig(null, strClass);
        }
        return ret;
    }
    
    /**
     * 通过类型获取获取方法名称
     * 
     * @param key 类/键值
     * @return
     */
    public String getJdbcResultMethod(Class<?> key)
    {
        String val = jdbcResultMethodMap.get(key);
        if (val != null)
            return val;
        return "getObject";
    }
    
    /**
     * 注册方法名称映射
     * 
     * @param key 类/键值
     * @param value 方法名称
     */
    private void registerJdbcResultMethod(Class<?> key, String value)
    {
        jdbcResultMethodMap.put(key, value);
        
    }
    
    /**
     * 注册类名/类映射
     * 
     * @param cls 类
     * @param className 类名称
     */
    private void registerStringClass(Class<?> cls, String className)
    {
        stringClassMap.put(className, cls);
    }
    
    /**
     * 单台模式
     * 
     * @return TransformFactory实例
     */
    public static TransformFactory getInstance()
    {
        if (instance == null)
        {
            lock.lock();
            try
            {
                if (instance == null)
                    instance = new TransformFactory();
            }
            finally
            {
                lock.unlock();
            }
        }
        return instance;
    }
    
    /**
     * 将结果集转换成实体并保存到列表集合中
     * 
     * @param <T> 类范型
     * @param c 实体类
     * @param sql SQL查询语句
     * @param rs 结果集
     * @return 返回实体类集合
     * @throws Exception
     */
    public <T> List<T> ResultToList(Class<T> c, String sql, ResultSet rs)
        throws Exception
    {
        List<T> list = new ArrayList<T>();
        while (rs.next())
        {
            T n = ResultToBean(c, sql, rs, false);
            list.add(n);
        }
        
        return list;
    }
    
    /**
     * 将结果集转换成实体类，并进行分页，将分页后的结果返回
     * 
     * @param <T> 实体类范型
     * @param c 实体类
     * @param sql SQL查询语句
     * @param rs 结果集
     * @param page 当前页数
     * @param pageSize 分页大小
     * @return 实体类列表
     * @throws Exception
     */
    public <T> List<T> ResultToPageScroll(Class<T> c, String sql, ResultSet rs, int page, int pageSize)
        throws Exception
    {
        List<T> list = new ArrayList<T>();
        int cur = (page - 1) * pageSize + 1;
        rs.absolute(cur);
        for (int i = 0; i < pageSize; i++)
        {
            T n = ResultToBean(c, sql, rs, false);
            list.add(n);
            if (!rs.next())
                break;
        }
        return list;
    }
    
    /**
     * 将结果集转换成实体类，并进行分页，将分页后的结果返回
     * 
     * @param <T> 实体类范型
     * @param c 实体类
     * @param sql SQL查询语句
     * @param rs 结果集
     * @param page 当前页数
     * @param pageSize 分页大小
     * @return 实体类列表
     * @throws Exception
     */
    public <T> List<T> ResultToPageForward(Class<T> c, String sql, ResultSet rs, int page, int pageSize)
        throws Exception
    {
        List<T> list = new ArrayList<T>();
        if (!rs.next())
            return list;
        int cur = (page - 1) * pageSize;
        for (int i = 0; i < cur; i++)
        {
            if (!rs.next())
                return list;
        }
        for (int i = 0; i < pageSize; i++)
        {
            T n = ResultToBean(c, sql, rs, false);
            list.add(n);
            if (!rs.next())
                break;
        }
        return list;
    }
    
    /**
     * 将结果转换成实体，单条记录
     * 
     * @param <T> 类范型
     * @param c 实体类
     * @param sql SQL查询语句
     * @param rs 结果集
     * @return 类实体
     * @throws Exception
     */
    public <T> T ResultToBean(Class<T> c, String sql, ResultSet rs, boolean cursorNext)
        throws Exception
    {
        if (DataTypeHelper.checkClassIsBasicDataType(c))
        {
            return ResultSetToBasicDataType(c, rs, cursorNext);
        }
        return ResultToEntityDataType(c, sql, rs, cursorNext, true, null);
    }
    
    /**
     * 将结果转换成实体，单条记录
     * 
     * @param <T> 类范型
     * @param c 实体类
     * @param sql SQL查询语句
     * @param rs 结果集
     * @param recursion 是否递归
     * @return 类实体
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T ResultToEntityDataType(Class<T> c, String sql, ResultSet rs, boolean cursorNext, boolean recursion,
        String alias)
        throws Exception
    {
        sql = sql.toUpperCase();
        boolean flag = c.isAnnotationPresent(Entity.class);
        if (!flag)
            return null;
        Entity classType = (Entity)c.getAnnotation(Entity.class);
        if (classType == null)
            return null;
        
        if (cursorNext)
        {
            if (!rs.next())
                return null;
        }
        
        if (!rs.isAfterLast() && !rs.isBeforeFirst())
        {
            EntityData entityData = ClassFactory.parseClass(c);
            if (entityData == null)
                return null;
            c = (Class<T>)entityData.getClassProxy();
            Object entity = entityData.newInstance();
            
            if (entity == null)
                return null;
            
            // 解析结果集所有列
            Map<String, Integer> colsIndexMap = cacheSqlColumnIndex(rs, sql);
            // 遍历实体类所有属性
            for (Entry<String, FieldData> entry : entityData.getMapFields().entrySet())
            {
                String key = entry.getKey();
                if (alias != null)
                {
                    key = alias + "_" + key;
                }
                FieldData fdata = entry.getValue();
                if (!fdata.isQueryable() || fdata.getFieldFlag() == FieldFlag.Collection)
                    continue;
                if (!colsIndexMap.containsKey(key))
                {
                    continue;
                }
                
                // 外键关联实体处理
                if (fdata.getFieldFlag() == FieldFlag.FkEntity)
                {
                    // 获取外键实体数据
                    EntityData fkData = fdata.getFkData();
                    if (fkData == null)
                        continue;
                    FieldData fpkdata = fkData.getPkField();
                    if (fpkdata == null)
                        continue;
                    String tname = fkData.getTableName();
                    tname = tname.toUpperCase();
                    if (sql.indexOf(" " + tname + " ") >= 0 && !tname.equals(entityData.getTableName()) && recursion)
                    { // 判断该SQL语句是否查询了该表
                        Class<?> fc = fdata.getFkClass();
                        // 递归转换外键实体对象
                        Object obj = this.ResultToEntityDataType(fc, sql, rs, false, false, alias);
                        fdata.setValue(entity, obj);
                    }
                    else
                    {
                        // 将外键值存入隐藏字段
                        Object obj = ReflectHelper.getResultSetValue(rs, fdata.getFieldClass(), key);
                        if (obj == null && DataTypeHelper.checkClassIsNumberBasicDataType(fpkdata.getFieldClass()))
                        {
                            obj = 0;
                        }
                        String fname = fdata.getFkSetMethod();
                        FastMethod fm = ReflectHelper.parseFastMethod(c, fname, fpkdata.getFieldClass());
                        fm.invoke(entity, new Object[] {obj});
                    }
                    continue;
                }
                // 处理非外键字段
                Object obj = ReflectHelper.getResultSetValue(rs, fdata.getFieldClass(), key);
                if (obj == null && DataTypeHelper.checkClassIsNumberBasicDataType(fdata.getFieldClass()))
                {
                    obj = 0;
                }
                fdata.setValue(entity, obj);
                
            }
            return (T)entity;
        }
        return null;
    }
    
    /**
     * 结果集转换至基本数据类型
     * 
     * @param c 基本数据类
     * @param rs 结果集
     * @param corsorNext 是否下移光标
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T ResultSetToBasicDataType(Class<T> c, ResultSet rs, boolean cursorNext)
        throws TransformException
    {
        if (cursorNext)
        {
            try
            {
                if (!rs.next())
                    return null;
            }
            catch (SQLException e)
            {
                throw new TransformException("TransformFactory::ResultSetToBasicDataType " + e.toString(), e);
            }
        }
        return (T)ReflectHelper.getResultSetValue(rs, c, 1);
    }
    
    /**
     * 级联查询转换SQL语句
     * 
     * @param sql 原SQL语句
     * @param c 返回类
     * @param map 级联表
     * @param pkey 键值
     * @return
     * @throws Exception
     */
    public <T> String transformSQLToCascade(String sql, Class<T> c, Map<String, String> map, StringBuffer pkey)
        throws Exception
    {
        sql = sql.toUpperCase();
        pkey.append("[SQL]");
        pkey.append(sql);
        pkey.append("[C]");
        pkey.append(c.getName());
        String key = pkey.toString();
        if (sqlTranMap.containsKey(key))
        {
            return sqlTranMap.get(key);
        }
        if (sql.indexOf("SELECT") >= 0)
        {
            int from = sql.indexOf("FROM");
            String fieldSql = sql.substring(0, from);
            if (fieldSql.indexOf("*") >= 0)
            {
                sql = sql.substring(from);
                // 获得实体类数据
                EntityData entityData = ClassFactory.parseClass(c);
                ConcurrentMap<String, FieldData> mapNameFields = entityData.getMapNameFields();
                StringBuffer buf = new StringBuffer(256);
                buf.append("SELECT ");
                // 获取结果实体类字段字符串
                if (!map.containsKey(SqlSession.SELF))
                {
                    buf.append(entityData.getFieldString());
                    buf.append(",");
                }
                else
                {
                    String alias = map.get(SqlSession.SELF);
                    buf.append(entityData.getAliasString(alias));
                    buf.append(",");
                }
                // 获取关联实体类字段字符串
                FieldData fieldData = null;
                for (Entry<String, String> entry : map.entrySet())
                {
                    String entityName = entry.getKey();
                    String alias = entry.getValue();
                    if (SqlSession.SELF.equals(entityName))
                        continue;
                    
                    if (entityName.indexOf(ENTITY_CASCADE_DIV) > 0)
                    {
                        String[] attrs = entityName.split(ENTITY_CASCADE_DIV);
                        EntityData data = entityData;
                        for (String attr : attrs)
                        {
                            ConcurrentMap<String, FieldData> dataFields = data.getMapNameFields();
                            fieldData = dataFields.get(attr);
                            data = fieldData.getFkData();
                        }
                    }
                    else
                    {
                        fieldData = mapNameFields.get(entityName);
                    }
                    
                    if (fieldData != null)
                    {
                        if (fieldData.getFieldFlag() == FieldFlag.FkEntity && fieldData.getFkData() != null)
                        {
                            EntityData fkData = fieldData.getFkData();
                            buf.append(fkData.getAliasString(alias, entityName));
                            buf.append(",");
                        }
                        else
                        {
                            throw new Exception("the '" + entityName + "' of Entity is not a foreign key field");
                        }
                    }
                    else
                    {
                        throw new Exception("Entity '" + entityData.getClassName() + "' does not include '"
                            + entityName + "' field");
                    }
                }
                if (buf.length() > 0)
                    buf.deleteCharAt(buf.length() - 1);
                buf.append(" ");
                buf.append(sql);
                sql = buf.toString();
                sqlTranMap.put(key, sql);
                return sql;
            }
            else
            {
                return sql;
            }
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 转换数量查询SQL
     * 
     * @param sql 原SQL
     * @return 转换后SQL
     */
    public String transformSqlToCount(String sql)
        throws Exception
    {
        sql = sql.toUpperCase();
        int selectIndex = sql.indexOf("SELECT");
        int fromIndex = sql.indexOf("FROM");
        if (selectIndex < 0 || fromIndex < 0)
        {
            throw new Exception("sql is error");
        }
        String tmp = sql.substring(fromIndex);
        StringBuffer buf = new StringBuffer(tmp.length() + 17);
        buf.append("SELECT COUNT(*) ");
        buf.append(tmp);
        // buf.append(") ");
        // buf.append(SQL_COUNT_ALIAS);
        return buf.toString();
    }
    
    /**
     * 级联将结果集转换成实体并保存到列表集合中
     * 
     * @param <T> 类范型
     * @param c 实体类
     * @param sql SQL查询语句
     * @param rs 结果集
     * @return 返回实体类集合
     * @throws Exception
     */
    public <T> List<T> cascadeResultToList(Class<T> c, String sql, ResultSet rs, Map<String, String> map)
        throws Exception
    {
        if (rs == null)
            return null;
        sql = sql.toUpperCase();
        boolean flag = c.isAnnotationPresent(Entity.class);
        if (!flag)
            return null;
        List<T> list = new ArrayList<T>();
        Entity classType = (Entity)c.getAnnotation(Entity.class);
        if (classType == null)
            return null;
        ClassFactory.parseClass(c);
        
        // c=ClassFactory.getClass(c.getName());
        while (rs.next())
        {
            T n = cascadeResultToBean(c, sql, rs, map, false);
            list.add(n);
        }
        
        return list;
    }
    
    /**
     * 级联将结果集转换成实体类，并进行分页，将分页后的结果返回
     * 
     * @param <T> 实体类范型
     * @param c 实体类
     * @param sql SQL查询语句
     * @param rs 结果集
     * @param page 当前页数
     * @param pageSize 分页大小
     * @return 实体类列表
     * @throws Exception
     */
    public <T> List<T> cascadeResultToPage(Class<T> c, String sql, ResultSet rs, int page, int pageSize,
        Map<String, String> map)
        throws Exception
    {
        sql = sql.toUpperCase();
        boolean flag = c.isAnnotationPresent(Entity.class);
        if (!flag)
            return null;
        List<T> list = new ArrayList<T>();
        Entity classType = (Entity)c.getAnnotation(Entity.class);
        if (classType == null)
            return null;
        ClassFactory.parseClass(c);
        
        int cur = (page - 1) * pageSize + 1;
        rs.absolute(cur);
        for (int i = 0; i < pageSize; i++)
        {
            
            T n = cascadeResultToBean(c, sql, rs, map, false);
            list.add(n);
            
            if (!rs.next())
                break;
        }
        return list;
    }
    
    /**
     * 级联将结果转换成实体，单条记录
     * 
     * @param <T> 类范型
     * @param c 实体类
     * @param sql SQL查询语句
     * @param rs 结果集
     * @return 类实体
     * @throws Exception
     */
    public <T> T cascadeResultToBean(Class<T> c, String sql, ResultSet rs, Map<String, String> map, boolean corsorNext)
        throws Exception
    {
        return this.cascadeResultToBean(c, sql, rs, map, corsorNext, null);
    }
    
    /**
     * 级联将结果转换成实体，单条记录
     * 
     * @param <T> 类范型
     * @param c 实体类
     * @param sql SQL查询语句
     * @param rs 结果集
     * @return 类实体
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T cascadeResultToBean(Class<T> c, String sql, ResultSet rs, Map<String, String> map, boolean corsorNext,
        String alias)
        throws Exception
    {
        sql = sql.toUpperCase();
        boolean flag = c.isAnnotationPresent(Entity.class);
        if (!flag)
            return null;
        Entity classType = (Entity)c.getAnnotation(Entity.class);
        if (classType == null)
            return null;
        EntityData entityData = ClassFactory.parseClass(c);
        if (corsorNext)
        {
            if (!rs.next())
                return null;
        }
        if (!rs.isAfterLast() && !rs.isBeforeFirst())
        {
            c = (Class<T>)ClassFactory.getClass(c.getName());
            Object entity = entityData.newInstance();
            // -------
            Map<String, Integer> colsIndexMap = cacheSqlColumnIndex(rs, sql);
            // --------
            for (Entry<String, FieldData> entry : entityData.getMapFields().entrySet())
            {
                String key = entry.getKey();
                if (alias != null)
                {
                    key = alias.toUpperCase() + "_" + key;
                }
                FieldData fdata = entry.getValue();
                
                if (!fdata.isQueryable() || fdata.getFieldFlag() == FieldFlag.Collection)
                {
                    continue;
                }
                if (!fdata.isQueryable())
                    continue;
                if (sql.indexOf(key.toUpperCase()) < 0 && sql.indexOf('*') < 0)
                {
                    continue;
                }
                
                if (!colsIndexMap.containsKey(key))
                {
                    continue;
                }
                
                if (fdata.getFieldFlag() == FieldFlag.FkEntity)
                {
                    EntityData fkData = fdata.getFkData();
                    if (fkData == null)
                        continue;
                    FieldData fpkdata = fkData.getPkField();
                    if (fpkdata == null)
                        continue;
                    Object obj = ReflectHelper.getResultSetValue(rs, fdata.getFieldClass(), key);
                    if (obj == null)
                    {
                        continue;
                    }
                    String fname = fdata.getFkSetMethod();
                    FastMethod fm = ReflectHelper.parseFastMethod(c, fname, fpkdata.getFieldClass());
                    fm.invoke(entity, new Object[] {obj});
                    // -------------------
                    String check = fdata.getFieldName(alias);
                    if (map.containsKey(check))
                    {
                        Class<?> fc = fdata.getFkClass();
                        obj = cascadeResultToBean(fc, sql, rs, map, false, fdata.getFieldName(alias));
                        fdata.setValue(entity, obj);
                    }
                    continue;
                }
                Object obj = ReflectHelper.getResultSetValue(rs, fdata.getFieldClass(), key);
                fdata.setValue(entity, obj);
            }
            return (T)entity;
        }
        return null;
    }
    
    /**
     * 缓存结果集列数据
     * 
     * @param rs 结果集
     * @param sql SQL语句 键值
     * @return
     * @throws SQLException
     */
    public Map<String, Integer> cacheSqlColumnIndex(ResultSet rs, String sql)
        throws SQLException
    {
        Map<String, Integer> colsIndexMap = colsMap.get(sql);
        if (colsIndexMap == null)
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colsCount = rsmd.getColumnCount();
            colsIndexMap = new HashMap<String, Integer>(colsCount);
            for (int i = 1; i <= rsmd.getColumnCount(); i++)
            {
                String key = rsmd.getColumnLabel(i).toUpperCase();
                colsIndexMap.put(key, i);
            }
            colsMap.put(sql, colsIndexMap);
        }
        return colsIndexMap;
    }
}