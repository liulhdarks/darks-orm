/**
 * 类名:JdbcDataSource.java
 * 作者:刘力华
 * 创建时间:2012-5-27
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

public class JdbcDataSource implements DataSource
{
    
    private String url;
    
    private String username;
    
    private String password;
    
    public JdbcDataSource()
    {
        
    }
    
    public JdbcDataSource(String url, String username, String password)
    {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    
    @Override
    public Connection getConnection()
        throws SQLException
    {
        return DriverManager.getConnection(url, username, password);
    }
    
    @Override
    public Connection getConnection(String username, String password)
        throws SQLException
    {
        return null;
    }
    
    @Override
    public PrintWriter getLogWriter()
        throws SQLException
    {
        return null;
    }
    
    @Override
    public int getLoginTimeout()
        throws SQLException
    {
        return 0;
    }
    
    @Override
    public void setLogWriter(PrintWriter out)
        throws SQLException
    {
        
    }
    
    @Override
    public void setLoginTimeout(int seconds)
        throws SQLException
    {
        
    }
    
    @Override
    public boolean isWrapperFor(Class<?> iface)
        throws SQLException
    {
        return false;
    }
    
    @Override
    public <T> T unwrap(Class<T> iface)
        throws SQLException
    {
        return null;
    }
    
}
