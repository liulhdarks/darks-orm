package darks.orm.datasource.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import darks.orm.core.config.SpringDataSourceConfiguration;
import darks.orm.datasource.ConnectionHandler;
import darks.orm.exceptions.DataSourceException;

public class ConnectionFactoryFailureTest
{

    @Test
    public void springConnectionDoesNotFallBackWhenPrimaryDataSourceFails()
    {
        SpringConnectionFactory factory = new SpringConnectionFactory();
        factory.setDataSourceConfig(new SpringDataSourceConfiguration(failingDataSource()));

        assertFailureDoesNotCallFallback(factory);
    }

    @Test
    public void jdbcConnectionDoesNotFallBackWhenPrimaryDataSourceFails()
    {
        JdbcConnectionFactory factory = new JdbcConnectionFactory();
        factory.setDataSourceConfig(new SpringDataSourceConfiguration(failingDataSource()));

        assertFailureDoesNotCallFallback(factory);
    }

    @Test
    public void jndiConnectionDoesNotFallBackWhenPrimaryDataSourceFails()
    {
        JndiConnectionFactory factory = new JndiConnectionFactory();
        factory.setDataSourceConfig(new SpringDataSourceConfiguration(failingDataSource()));

        assertFailureDoesNotCallFallback(factory);
    }

    @Test
    public void boneCpConnectionDoesNotFallBackWhenPrimaryDataSourceFails()
    {
        BoneCPConnectionFactory factory = new BoneCPConnectionFactory();
        factory.setDataSourceConfig(new SpringDataSourceConfiguration(failingDataSource()));

        assertFailureDoesNotCallFallback(factory);
    }

    @Test
    public void explicitDataSourceDoesNotFallBackWhenPrimaryDataSourceFails()
    {
        DataSourceFactory factory = new DataSourceFactory(failingDataSource());

        assertFailureDoesNotCallFallback(factory);
    }

    private void assertFailureDoesNotCallFallback(ConnectionHandler factory)
    {
        FallbackHandler fallback = new FallbackHandler();
        factory.setHandler(fallback);

        try
        {
            factory.getConnection();
            fail("Expected primary data source failure to be propagated");
        }
        catch (DataSourceException expected)
        {
            assertFalse(fallback.called);
        }
    }

    private DataSource failingDataSource()
    {
        return (DataSource)Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] {DataSource.class},
            new InvocationHandler()
            {
                public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable
                {
                    if ("getConnection".equals(method.getName()))
                    {
                        throw new SQLException("primary datasource unavailable");
                    }
                    if ("toString".equals(method.getName()))
                    {
                        return "failingDataSource";
                    }
                    if ("hashCode".equals(method.getName()))
                    {
                        return Integer.valueOf(System.identityHashCode(proxy));
                    }
                    if ("equals".equals(method.getName()))
                    {
                        return Boolean.valueOf(proxy == args[0]);
                    }
                    return null;
                }
            });
    }

    private static class FallbackHandler extends ConnectionHandler
    {
        boolean called;

        @Override
        public Connection getConnection()
        {
            called = true;
            return null;
        }
    }
}
