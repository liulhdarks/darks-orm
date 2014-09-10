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

package darks.orm.core.data;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import darks.orm.core.data.FieldData.FieldFlag;
import darks.orm.core.factory.ProxyBeanFactory;
import darks.orm.util.ReflectHelper;

@SuppressWarnings("unchecked")
public class EntityData implements Serializable
{
    
    private static final long serialVersionUID = 719863136319493577L;
    
    // 属性存储表
    private ConcurrentMap<String, FieldData> mapFields = null; // KEY为列名
    
    private ConcurrentMap<String, FieldData> mapNameFields = null; // Key为属性名
    
    private ConcurrentMap<String, MethodData> mapMethods = null;
    
    // 数据课表名
    private String tableName;
    
    // 类名称
    private String className;
    
    // 域
    private String schema;
    
    // 实体类
    private Class<?> classOrignal;
    
    // 实体代理类
    private Class<?> classProxy;
    
    // 主键字段
    private FieldData pkField;
    
    // 查询字段字符串
    private String fieldString;
    
    private boolean serializable = false;
    
    private boolean useProxy = false;
    
    // 字段属性字符串 u.user_id
    private ConcurrentMap<String, String> mapFieldsAlias = null;
    
    // 字段属性字符串 u.user_id sender_user_id
    private ConcurrentMap<String, ConcurrentMap<String, String>> mapFieldsAliasName = null;
    
    public EntityData()
    {
        mapFields = new ConcurrentHashMap<String, FieldData>();
        mapNameFields = new ConcurrentHashMap<String, FieldData>();
        mapFieldsAlias = new ConcurrentHashMap<String, String>();
        mapFieldsAliasName = new ConcurrentHashMap<String, ConcurrentMap<String, String>>();
        mapMethods = new ConcurrentHashMap<String, MethodData>();
    }
    
    public EntityData(String tableName, String className, String schema, Class<?> classOrignal, Class<?> classProxy)
    {
        super();
        this.tableName = tableName;
        this.className = className;
        this.schema = schema;
        this.classOrignal = classOrignal;
        this.classProxy = classProxy;
    }
    
    public void addMethod(MethodData methodData)
    {
        mapMethods.put(methodData.getMethodName(), methodData);
    }
    
    /**
     * 添加属性数据
     * 
     * @param fieldData 属性实例
     */
    public void addField(FieldData fieldData)
    {
        mapFields.put(fieldData.getColumnName().toUpperCase(), fieldData);
        mapNameFields.put(fieldData.getFieldName(), fieldData);
    }
    
    /**
     * 添加主键属性数据
     * 
     * @param fieldData 属性实例
     */
    public void addPkField(FieldData fieldData)
    {
        pkField = fieldData;
        mapFields.put(fieldData.getColumnName().toUpperCase(), fieldData);
        mapNameFields.put(fieldData.getFieldName(), fieldData);
    }
    
    /**
     * 去除属性数据
     * 
     * @param fieldName 属性
     */
    public void removeField(String fieldName)
    {
        mapFields.remove(fieldName);
    }
    
    public String getFieldString()
    {
        if (fieldString == null)
        {
            StringBuffer buf = new StringBuffer();
            for (Entry<String, FieldData> entry : mapFields.entrySet())
            {
                String key = entry.getKey();
                FieldData f = entry.getValue();
                if (!f.isQueryable())
                    continue;
                if (f.getFieldFlag() == FieldFlag.Collection)
                {
                    continue;
                }
                
                buf.append(key);
                buf.append(",");
            }
            if (buf.length() > 0)
                buf.deleteCharAt(buf.length() - 1);
            fieldString = buf.toString();
        }
        return fieldString;
    }
    
    public String getAliasString(String alias)
    {
        if (mapFieldsAlias.containsKey(alias))
        {
            return mapFieldsAlias.get(alias);
        }
        StringBuffer buf = new StringBuffer();
        for (Entry<String, FieldData> entry : mapFields.entrySet())
        {
            String key = entry.getKey();
            FieldData f = entry.getValue();
            if (!f.isQueryable())
                continue;
            if (f.getFieldFlag() == FieldFlag.Collection)
            {
                continue;
            }
            // u.user_id
            buf.append(alias);
            buf.append(".");
            buf.append(key);
            buf.append(",");
        }
        if (buf.length() > 0)
            buf.deleteCharAt(buf.length() - 1);
        String ret = buf.toString();
        mapFieldsAlias.put(alias, ret);
        return ret;
    }
    
    public String getAliasString(String alias, String entityName)
    {
        ConcurrentMap<String, String> names = null;
        if (mapFieldsAliasName.containsKey(alias))
        {
            names = mapFieldsAliasName.get(alias);
            if (names.containsKey(entityName))
            {
                return names.get(entityName);
            }
        }
        else
        {
            names = new ConcurrentHashMap<String, String>();
            mapFieldsAliasName.put(alias, names);
        }
        StringBuffer buf = new StringBuffer();
        for (Entry<String, FieldData> entry : mapFields.entrySet())
        {
            String key = entry.getKey();
            FieldData f = entry.getValue();
            if (!f.isQueryable())
                continue;
            if (f.getFieldFlag() == FieldFlag.Collection)
            {
                continue;
            }
            // u.user_id sender_user_id
            buf.append(alias);
            buf.append(".");
            buf.append(key);
            buf.append(" ");
            buf.append(entityName);
            buf.append("_");
            buf.append(key);
            buf.append(",");
        }
        if (buf.length() > 0)
            buf.deleteCharAt(buf.length() - 1);
        String ret = buf.toString();
        if (names != null)
        {
            names.put(entityName, ret);
        }
        return ret;
    }
    
    public <T> T newInstance()
    {
        Class<T> c = (Class<T>)getClassProxy();
        T n = null;
        if (isUseProxy())
        {
            n = ProxyBeanFactory.getProxyEntity(c);
        }
        else
        {
            n = ReflectHelper.newFastInstance(c);
        }
        return n;
    }
    
    public ConcurrentMap<String, FieldData> getMapFields()
    {
        return mapFields;
    }
    
    public void setMapFields(ConcurrentMap<String, FieldData> mapFields)
    {
        this.mapFields = mapFields;
    }
    
    public String getClassName()
    {
        return className;
    }
    
    public void setClassName(String className)
    {
        this.className = className;
    }
    
    public String getSchema()
    {
        return schema;
    }
    
    public void setSchema(String schema)
    {
        this.schema = schema;
    }
    
    public Class<?> getClassOrignal()
    {
        return classOrignal;
    }
    
    public void setClassOrignal(Class<?> classOrignal)
    {
        this.classOrignal = classOrignal;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
    public Class<?> getClassProxy()
    {
        return classProxy;
    }
    
    public void setClassProxy(Class<?> classProxy)
    {
        this.classProxy = classProxy;
    }
    
    public FieldData getPkField()
    {
        return pkField;
    }
    
    public void setPkField(FieldData pkField)
    {
        this.pkField = pkField;
    }
    
    public ConcurrentMap<String, FieldData> getMapNameFields()
    {
        return mapNameFields;
    }
    
    public void setMapNameFields(ConcurrentMap<String, FieldData> mapNameFields)
    {
        this.mapNameFields = mapNameFields;
    }
    
    public boolean isUseProxy()
    {
        return useProxy;
    }
    
    public void setUseProxy(boolean useProxy)
    {
        this.useProxy = useProxy;
    }
    
    public boolean isSerializable()
    {
        return serializable;
    }
    
    public void setSerializable(boolean serializable)
    {
        this.serializable = serializable;
    }
    
}