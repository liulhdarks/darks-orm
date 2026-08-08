package darks.orm.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

import org.junit.Test;

public class ReflectHelperWasNullTest
{

    @Test
    public void wrapperNumericNullRemainsNull()
        throws Exception
    {
        ResultSet rs = resultSet(0, true);
        assertNull(ReflectHelper.getResultSetValue(rs, Integer.class, 1));
        assertNull(ReflectHelper.getResultSetValue(rs, Long.class, "COL"));
        assertNull(ReflectHelper.getResultSetValue(rs, Boolean.class, 1));
    }

    @Test
    public void wrapperNumericNonNullKeepsValue()
        throws Exception
    {
        ResultSet rs = resultSet(5, false);
        assertEquals(Integer.valueOf(5), ReflectHelper.getResultSetValue(rs, Integer.class, 1));
        assertEquals(Integer.valueOf(5), ReflectHelper.getResultSetValue(rs, Integer.class, "COL"));
    }

    @Test
    public void primitiveNullStillReturnsDefault()
        throws Exception
    {
        ResultSet rs = resultSet(0, true);
        assertEquals(Integer.valueOf(0), ReflectHelper.getResultSetValue(rs, int.class, 1));
    }

    private static ResultSet resultSet(final int intValue, final boolean wasNull)
    {
        return (ResultSet)Proxy.newProxyInstance(ResultSet.class.getClassLoader(),
            new Class[] {ResultSet.class}, new InvocationHandler()
            {
                public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable
                {
                    String name = method.getName();
                    if ("wasNull".equals(name))
                    {
                        return Boolean.valueOf(wasNull);
                    }
                    if ("getInt".equals(name))
                    {
                        return Integer.valueOf(intValue);
                    }
                    if ("getLong".equals(name))
                    {
                        return Long.valueOf(intValue);
                    }
                    if ("getBoolean".equals(name))
                    {
                        return Boolean.valueOf(intValue != 0);
                    }
                    if ("getObject".equals(name))
                    {
                        return wasNull ? null : Integer.valueOf(intValue);
                    }
                    if ("toString".equals(name))
                    {
                        return "StubResultSet";
                    }
                    if ("hashCode".equals(name))
                    {
                        return Integer.valueOf(System.identityHashCode(proxy));
                    }
                    if ("equals".equals(name))
                    {
                        return Boolean.valueOf(proxy == args[0]);
                    }
                    Class<?> returnType = method.getReturnType();
                    if (returnType.equals(boolean.class))
                    {
                        return Boolean.FALSE;
                    }
                    if (returnType.equals(int.class))
                    {
                        return Integer.valueOf(0);
                    }
                    if (returnType.equals(long.class))
                    {
                        return Long.valueOf(0L);
                    }
                    if (returnType.equals(double.class))
                    {
                        return Double.valueOf(0D);
                    }
                    if (returnType.equals(float.class))
                    {
                        return Float.valueOf(0F);
                    }
                    if (returnType.equals(short.class))
                    {
                        return Short.valueOf((short)0);
                    }
                    if (returnType.equals(byte.class))
                    {
                        return Byte.valueOf((byte)0);
                    }
                    return null;
                }
            });
    }
}
