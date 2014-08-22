package darks.orm.datasource.factory;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import darks.orm.core.config.DataSourceConfiguration;
import darks.orm.datasource.ConnectionHandler;
import darks.orm.exceptions.DataSourceException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

public class JndiConnectionFactory extends ConnectionHandler
{
    
    private static final Logger logger = LoggerFactory.getLogger(JndiConnectionFactory.class);
    
    /**
     * 获得JNDI连接
     * 
     * @return JNDI连接
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws NamingException
     * @throws SQLException
     */
    public Connection getJndiConnection()
        throws ClassNotFoundException, SQLException
    {
        logger.debug("Get JNDI Connection");
        DataSourceConfiguration config = this.getDataSourceConfig();
        DataSource ds = config.getDataSource();
        return ds.getConnection();
    }
    
    public Connection getConnection()
    {
        try
        {
            Connection conn = getJndiConnection();
            if (conn == null)
            {
                throw new DataSourceException("getConnection connection is null");
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
