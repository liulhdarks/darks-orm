package darks.orm.test;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import darks.orm.app.SqlSession;
import darks.orm.core.factory.SqlSessionFactory;
import darks.orm.test.model.User;



public class QueryTest
{
    
    SqlSession session = null;
    
    @Before
    public void before()
    {
        session = SqlSessionFactory.getSession();
        Assert.assertNotNull(session);
    }
    
    @After
    public void after()
    {
        session.close();
    }
    
    @Test
    public void testQuery()
    {
        List<User> users = session.queryList(User.class, "select * from users");
        Assert.assertNotNull(session);
        Assert.assertFalse(users.isEmpty());
        
    }
    
}
