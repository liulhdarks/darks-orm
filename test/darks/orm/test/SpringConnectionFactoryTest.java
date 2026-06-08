package darks.orm.test;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.junit.Test;

import darks.orm.core.config.SpringDataSourceConfiguration;
import darks.orm.datasource.factory.SpringConnectionFactory;
import darks.orm.exceptions.DataSourceException;

public class SpringConnectionFactoryTest
{
    
    @Test(expected = DataSourceException.class)
    public void shouldFailFastWhenSpringDataSourceCannotCreateConnection()
    {
        SpringConnectionFactory factory = new SpringConnectionFactory();
        factory.setDataSourceConfig(new SpringDataSourceConfiguration(new FailingDataSource()));
        
        factory.getConnection();
    }
    
    private static class FailingDataSource implements DataSource
    {
        
        public Connection getConnection()
            throws SQLException
        {
            throw new SQLException("primary datasource unavailable");
        }
        
        public Connection getConnection(String username, String password)
            throws SQLException
        {
            throw new SQLException("primary datasource unavailable");
        }
        
        public PrintWriter getLogWriter()
            throws SQLException
        {
            return null;
        }
        
        public void setLogWriter(PrintWriter out)
            throws SQLException
        {
        }
        
        public void setLoginTimeout(int seconds)
            throws SQLException
        {
        }
        
        public int getLoginTimeout()
            throws SQLException
        {
            return 0;
        }
        
        public Logger getParentLogger()
            throws SQLFeatureNotSupportedException
        {
            throw new SQLFeatureNotSupportedException();
        }
        
        public <T> T unwrap(Class<T> iface)
            throws SQLException
        {
            throw new SQLException("not a wrapper");
        }
        
        public boolean isWrapperFor(Class<?> iface)
            throws SQLException
        {
            return false;
        }
    }
}
