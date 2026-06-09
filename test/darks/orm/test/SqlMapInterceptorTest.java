package darks.orm.test;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import darks.orm.app.Page;
import darks.orm.app.QueryEnumType;
import darks.orm.core.interceptor.SqlMapInterceptor;
import darks.orm.test.model.User;

public class SqlMapInterceptorTest
{
    
    interface Mapper
    {
        List<User> listUsers();
        
        Collection<User> collectionUsers();
        
        Page<User> pageUsers();
        
        User singleUser();
    }
    
    @Test
    public void parseAutoQueryTypeDetectsCollectionReturns()
        throws Exception
    {
        Assert.assertEquals(QueryEnumType.List, parseAutoQueryType("listUsers"));
        Assert.assertEquals(QueryEnumType.List, parseAutoQueryType("collectionUsers"));
    }
    
    @Test
    public void parseAutoQueryTypeDetectsPageReturns()
        throws Exception
    {
        Assert.assertEquals(QueryEnumType.Page, parseAutoQueryType("pageUsers"));
    }
    
    @Test
    public void parseAutoQueryTypeDefaultsToObjectReturns()
        throws Exception
    {
        Assert.assertEquals(QueryEnumType.Object, parseAutoQueryType("singleUser"));
    }
    
    private QueryEnumType parseAutoQueryType(String mapperMethodName)
        throws Exception
    {
        Method parser = SqlMapInterceptor.class.getDeclaredMethod("parseAutoQueryType", Method.class);
        parser.setAccessible(true);
        Method mapperMethod = Mapper.class.getDeclaredMethod(mapperMethodName);
        return (QueryEnumType)parser.invoke(new SqlMapInterceptor(), mapperMethod);
    }
}
