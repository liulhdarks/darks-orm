/**
 * 类名:JdbcConfig.java
 * 作者:刘力华
 * 创建时间:2012-5-27
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.core.config;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import darks.orm.datasource.JdbcDataSource;

public class JdbcConfiguration extends DataSourceConfiguration
{
    
    private JdbcDataSource ds;
    
    private String driver;
    
    private String url;
    
    private String username;
    
    private String password;
    
    private volatile boolean isInited = false;
    
    private Lock lock = new ReentrantLock();
    
    public JdbcConfiguration()
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
                    Class.forName(getDriver());
                    ds = new JdbcDataSource(url, username, password);
                    isInited = true;
                }
            }
            finally
            {
                lock.unlock();
            }
        }
        return ds;
    }
    
    public String getDriver()
    {
        return driver;
    }
    
    public void setDriver(String driver)
    {
        this.driver = driver;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    @Override
    public void destroy()
    {
        ds = null;
        isInited = false;
    }
    
}
