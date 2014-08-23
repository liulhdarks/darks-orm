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

public class SqlParamData
{
    
    private static final int INIT_BUFFER_SIZE = 32;
    
    public static enum SqlParamEnumType
    {
        INDEX, ENTITY
    }
    
    private String value;
    
    private SqlParamData next;
    
    private SqlParamData last;
    
    private SqlParamEnumType type;
    
    public SqlParamData()
    {
        last = this;
    }
    
    public SqlParamData(String value)
    {
        setValue(value);
        last = this;
    }
    
    public void addNext(SqlParamData next)
    {
        SqlParamData cur = next();
        while (cur.next() != null)
        {
            cur = cur.next();
        }
        cur.setNext(next);
    }
    
    public void addLast(SqlParamData next)
    {
        if (last == null)
        {
            last = next;
            setNext(next);
        }
        else
        {
            last.setNext(next);
            last = next;
        }
    }
    
    public void addLastValue(String value)
    {
        if (getValue() == null)
        {
            setValue(value);
        }
        else
        {
            SqlParamData ndata = new SqlParamData(value);
            addLast(ndata);
        }
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public SqlParamData next()
    {
        return next;
    }
    
    public void setNext(SqlParamData next)
    {
        this.next = next;
        last = next;
    }
    
    public SqlParamData last()
    {
        return last;
    }
    
    public SqlParamEnumType getType()
    {
        return type;
    }
    
    public void setType(SqlParamEnumType type)
    {
        this.type = type;
    }
    
    @Override
    public String toString()
    {
        StringBuffer buf = new StringBuffer(INIT_BUFFER_SIZE);
        buf.append("[");
        buf.append(type);
        buf.append(":");
        buf.append(value);
        SqlParamData cur = next;
        while (cur != null)
        {
            buf.append(".");
            buf.append(cur.getValue());
            cur = cur.next();
        }
        buf.append("]");
        return buf.toString();
    }
    
}