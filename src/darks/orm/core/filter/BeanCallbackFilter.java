package darks.orm.core.filter;

import java.lang.reflect.Method;

import darks.orm.annotation.ManyToOne;
import darks.orm.annotation.OneToMany;
import darks.orm.annotation.OneToOne;
import darks.orm.annotation.Query;

import net.sf.cglib.proxy.CallbackFilter;

public class BeanCallbackFilter implements CallbackFilter
{
    
    public int accept(Method method)
    {
        
        if (method.isAnnotationPresent(OneToMany.class))
        {
            return 0;
        }
        if (method.isAnnotationPresent(OneToOne.class) || method.isAnnotationPresent(ManyToOne.class))
        {
            return 1;
        }
        if (method.isAnnotationPresent(Query.class))
        {
            return 2;
        }
        return 3;
    }
    
}
