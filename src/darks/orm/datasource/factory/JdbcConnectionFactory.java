package darks.orm.datasource.factory;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import darks.orm.core.config.DataSourceConfiguration;
import darks.orm.datasource.ConnectionHandler;
import darks.orm.exceptions.DataSourceException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.LogHelper;

public class JdbcConnectionFactory extends ConnectionHandler
{
    
    private static final Logger logger = LoggerFactory.getLogger(JdbcConnectionFactory.class);
    
    /**
     * 获得JDBC连接
     * 
     * @return JDBC连接
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Connection getJdbcConnection()
        throws SQLException, ClassNotFoundException
    {
        LogHelper.println(this, "[Thread " + Thread.currentThread().getId() + " NAME:"
            + Thread.currentThread().getName() + "]Get Jdbc Connection");
        DataSourceConfiguration config = this.getDataSourceConfig();
        DataSource ds = config.getDataSource();
        return ds.getConnection();
    }
    
    public Connection getConnection()
    {
        try
        {
            Connection conn = getJdbcConnection();
            if (conn == null)
            {
                LogHelper.except(logger,
                    "JdbcConnectionFactory::getConnection connection is null",
                    DataSourceException.class);
            }
            return conn;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return super.getConnection();
        }
    }
}
