package darks.orm.core.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import darks.orm.app.Page;
import darks.orm.app.QueryEnumType;

public class SqlMapQueryTypeResolverTest
{
    
    interface Mapper
    {
        List<String> listUsers();
        
        Collection<String> collectionUsers();
        
        Page<String> pageUsers();
        
        String singleUser();
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
        Method mapperMethod = Mapper.class.getDeclaredMethod(mapperMethodName);
        return SqlMapQueryTypeResolver.parseAutoQueryType(mapperMethod);
    }
}
