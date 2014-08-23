
package darks.orm.core.config;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JndiConfiguration extends DataSourceConfiguration
{
    
    private DataSource ds;
    
    private String jndiPoolName;
    
    private volatile boolean isInited = false;
    
    private Lock lock = new ReentrantLock();
    
    public JndiConfiguration()
    {
        
    }
    
    public DataSource getDataSource()
        throws ClassNotFoundException
    {
        if (!isInited)
        {
            lock.lock();
            try
            {
                if (!isInited)
                {
                    try
                    {
                        Context initContext = new InitialContext();
                        ds = (DataSource)initContext.lookup(jndiPoolName);
                        isInited = true;
                    }
                    catch (NamingException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            finally
            {
                lock.unlock();
            }
        }
        return ds;
    }
    
    @Override
    public void destroy()
    {
        ds = null;
        isInited = false;
    }
    
    public String getJndiPoolName()
    {
        return jndiPoolName;
    }
    
    public void setJndiPoolName(String jndiPoolName)
    {
        this.jndiPoolName = jndiPoolName;
    }
    
}
