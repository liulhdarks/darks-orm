package darks.orm.core.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class NoOpMethodInterceptor implements MethodInterceptor, Serializable
{
    
    private static final long serialVersionUID = 2394241980207790248L;
    
    @Override
    public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3)
        throws Throwable
    {
        return arg3.invokeSuper(arg0, arg2);
    }
    
}
