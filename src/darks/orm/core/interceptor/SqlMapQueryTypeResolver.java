package darks.orm.core.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;

import darks.orm.app.Page;
import darks.orm.app.QueryEnumType;

final class SqlMapQueryTypeResolver
{
    
    private SqlMapQueryTypeResolver()
    {
        
    }
    
    static QueryEnumType parseAutoQueryType(Method method)
    {
        Class<?> clazz = method.getReturnType();
        if (Collection.class.isAssignableFrom(clazz))
        {
            return QueryEnumType.List;
        }
        else if (Page.class.isAssignableFrom(clazz))
        {
            return QueryEnumType.Page;
        }
        else
        {
            return QueryEnumType.Object;
        }
    }
}
