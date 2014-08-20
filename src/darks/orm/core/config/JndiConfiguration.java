/**
 * 类名:JndiConfiguration.java
 * 作者:刘力华
 * 创建时间:2012-5-27
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

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
