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
import java.lang.reflect.Field;

import darks.orm.exceptions.ClassReflectException;

public class FieldData implements Serializable
{
    
    private static final long serialVersionUID = -1299801983747003203L;
    
    public enum FieldFlag
    {
        Normal, FkEntity, Collection
    };
    
    private PrimaryKeyData pkData;
    
    private boolean isPrimaryKey = false;
    
    private String fieldName;
    
    private transient Field field;
    
    private String columnName;
    
    private String fieldType;
    
    private Class<?> fieldClass;
    
    private boolean nullable = false;
    
    private boolean unique = false;
    
    private boolean insertable = true;
    
    private boolean updatable = true;
    
    private boolean queryable = true;
    
    private FieldFlag fieldFlag = FieldFlag.Normal;
    
    private String fkField;
    
    private String fkSetMethod;
    
    private String fkGetMethod;
    
    private Class<?> fkClass;
    
    private EntityData fkData;
    
    public FieldData()
    {
        
    }
    
    public void setValue(Object obj, Object value)
        throws ClassReflectException
    {
        field.setAccessible(true);
        try
        {
            field.set(obj, value);
        }
        catch (Exception e)
        {
            throw new ClassReflectException("FieldData::getValue " + e.toString(), e);
        }
    }
    
    public Object getValue(Object obj)
        throws ClassReflectException
    {
        field.setAccessible(true);
        try
        {
            return field.get(obj);
        }
        catch (Exception e)
        {
            throw new ClassReflectException("FieldData::getValue " + e.toString(), e);
        }
    }
    
    public String getFieldName()
    {
        return fieldName;
    }
    
    public String getFieldName(String alias)
    {
        if (alias != null)
        {
            return alias + "_" + fieldName;
        }
        return fieldName;
    }
    
    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }
    
    public Field getField()
    {
        return field;
    }
    
    public void setField(Field field)
    {
        this.field = field;
    }
    
    public String getColumnName()
    {
        return columnName;
    }
    
    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }
    
    public boolean isNullable()
    {
        return nullable;
    }
    
    public void setNullable(boolean nullable)
    {
        this.nullable = nullable;
    }
    
    public boolean isUnique()
    {
        return unique;
    }
    
    public void setUnique(boolean unique)
    {
        this.unique = unique;
    }
    
    public boolean isInsertable()
    {
        return insertable;
    }
    
    public void setInsertable(boolean insertable)
    {
        this.insertable = insertable;
    }
    
    public boolean isUpdatable()
    {
        return updatable;
    }
    
    public void setUpdatable(boolean updatable)
    {
        this.updatable = updatable;
    }
    
    public boolean isQueryable()
    {
        return queryable;
    }
    
    public void setQueryable(boolean queryable)
    {
        this.queryable = queryable;
    }
    
    public String getFieldType()
    {
        return fieldType;
    }
    
    public void setFieldType(String fieldType)
    {
        this.fieldType = fieldType;
    }
    
    public FieldFlag getFieldFlag()
    {
        return fieldFlag;
    }
    
    public void setFieldFlag(FieldFlag fieldFlag)
    {
        this.fieldFlag = fieldFlag;
    }
    
    public boolean isPrimaryKey()
    {
        return isPrimaryKey;
    }
    
    public void setPrimaryKey(boolean isPrimaryKey)
    {
        this.isPrimaryKey = isPrimaryKey;
    }
    
    public String getFkSetMethod()
    {
        return fkSetMethod;
    }
    
    public void setFkSetMethod(String fkSetMethod)
    {
        this.fkSetMethod = fkSetMethod;
    }
    
    public String getFkGetMethod()
    {
        return fkGetMethod;
    }
    
    public void setFkGetMethod(String fkGetMethod)
    {
        this.fkGetMethod = fkGetMethod;
    }
    
    public Class<?> getFkClass()
    {
        return fkClass;
    }
    
    public void setFkClass(Class<?> fkClass)
    {
        this.fkClass = fkClass;
    }
    
    public PrimaryKeyData getPkData()
    {
        return pkData;
    }
    
    public void setPkData(PrimaryKeyData pkData)
    {
        this.pkData = pkData;
    }
    
    public EntityData getFkData()
    {
        return fkData;
    }
    
    public void setFkData(EntityData fkData)
    {
        this.fkData = fkData;
    }
    
    public Class<?> getFieldClass()
    {
        return fieldClass;
    }
    
    public void setFieldClass(Class<?> fieldClass)
    {
        this.fieldClass = fieldClass;
    }
    
    public String getFkField()
    {
        return fkField;
    }
    
    public void setFkField(String fkField)
    {
        this.fkField = fkField;
    }
    
}