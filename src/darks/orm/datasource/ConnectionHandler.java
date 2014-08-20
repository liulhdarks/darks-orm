package darks.orm.datasource;

import java.sql.Connection;

import darks.orm.core.config.DataSourceConfiguration;
import darks.orm.datasource.factory.ConnectionFactory;

public abstract class ConnectionHandler implements IConnectionFactory
{
    
    private DataSourceConfiguration dsConfig;
    
    private ConnectionHandler handler = null;
    
    public Connection getConnection()
    {
        if (handler != null)
        {
            ConnectionFactory.getInstance().setFactory(handler);
            ConnectionFactory.getInstance().setCurrentDataSourceConfig(dsConfig);
            return handler.getConnection();
        }
        return null;
    }
    
    public ConnectionHandler getHandler()
    {
        return handler;
    }
    
    public void setHandler(ConnectionHandler handler)
    {
        this.handler = handler;
    }
    
    public DataSourceConfiguration getDataSourceConfig()
    {
        return dsConfig;
    }
    
    public void setDataSourceConfig(DataSourceConfiguration dsConfig)
    {
        this.dsConfig = dsConfig;
    }
    
}
