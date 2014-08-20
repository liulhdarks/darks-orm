/**
 * 类名:SqlParamData.java
 * 作者:刘力华
 * 创建时间:2012-6-9
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
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
