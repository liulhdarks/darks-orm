package darks.orm.util;

public final class DataTypeHelper
{
    
    private DataTypeHelper()
    {
        
    }
    
    public static Object longToOther(Object val, Class<?> dest)
    {
        if (val instanceof Long)
        {
            Long l = (Long)val;
            if (dest.equals(Integer.class) || dest.equals(int.class))
            {
                return l.intValue();
            }
            else if (dest.equals(Float.class) || dest.equals(float.class))
            {
                return l.floatValue();
            }
            else if (dest.equals(Double.class) || dest.equals(double.class))
            {
                return l.doubleValue();
            }
            else if (dest.equals(Byte.class) || dest.equals(byte.class))
            {
                return l.byteValue();
            }
            else if (dest.equals(Short.class) || dest.equals(short.class))
            {
                return l.shortValue();
            }
            else if (dest.equals(long.class))
            {
                return l.longValue();
            }
            else if (dest.equals(String.class))
            {
                return String.valueOf(l);
            }
        }
        return val;
    }
    
    /**
     * 检查基本数据类型的值是否为空
     * 
     * @param pkclass 类
     * @param val 值
     * @return
     */
    public static boolean checkValueIsNull(Class<?> pkclass, Object val)
    {
        if (val == null)
            return true;
        if ((pkclass.equals(int.class) || pkclass.equals(Integer.class)) && (Integer)val <= 0)
        {
            return true;
        }
        else if ((pkclass.equals(short.class) || pkclass.equals(Short.class)) && (Short)val <= 0)
        {
            return true;
        }
        else if (pkclass.equals(char.class) && (Integer)val <= 0)
        {
            return true;
        }
        else if ((pkclass.equals(long.class) || pkclass.equals(Long.class)) && (Long)val <= 0)
        {
            return true;
        }
        else if ((pkclass.equals(double.class) || pkclass.equals(Double.class)) && (Double)val <= 0)
        {
            return true;
        }
        else if ((pkclass.equals(float.class) || pkclass.equals(Float.class)) && (Float)val <= 0)
        {
            return true;
        }
        else if ((pkclass.equals(byte.class) || pkclass.equals(Byte.class)) && (Byte)val <= 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * 检查类是否为基本数据类型
     * 
     * @param cls 类
     * @return true 是 :false 不是
     */
    public static boolean checkClassIsBasicDataType(Class<?> cls)
    {
        if (cls == null)
            return false;
        if (cls.equals(int.class) || cls.equals(Integer.class))
        {
            return true;
        }
        else if (cls.equals(short.class) || cls.equals(Short.class))
        {
            return true;
        }
        else if (cls.equals(long.class) || cls.equals(Long.class))
        {
            return true;
        }
        else if (cls.equals(double.class) || cls.equals(Double.class))
        {
            return true;
        }
        else if (cls.equals(float.class) || cls.equals(Float.class))
        {
            return true;
        }
        else if (cls.equals(byte.class) || cls.equals(Byte.class))
        {
            return true;
        }
        return false;
    }
    
    /**
     * 检查类是否为基本数据类型
     * 
     * @param cls 类
     * @return true 是 :false 不是
     */
    public static boolean checkClassIsNumberBasicDataType(Class<?> cls)
    {
        if (cls == null)
            return false;
        if (cls.equals(int.class) || cls.equals(short.class) || cls.equals(long.class) || cls.equals(double.class)
            || cls.equals(float.class) || cls.equals(byte.class))
        {
            return true;
        }
        return false;
    }
}
