package darks.orm.datasource.factory;

import java.sql.Connection;

import javax.sql.DataSource;

import darks.orm.datasource.ConnectionHandler;

public class DataSourceFactory extends ConnectionHandler
{
    
    private DataSource dataSource;
    
    private static DataSourceFactory instance = null;
    
    public DataSourceFactory()
    {
        if (instance == null)
        {
            instance = this;
        }
    }
    
    public DataSourceFactory(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
    
    public static DataSourceFactory getInstance()
    {
        if (instance == null)
        {
            instance = new DataSourceFactory();
        }
        return instance;
    }
    
    @Override
    public Connection getConnection()
    {
        if (dataSource == null)
        {
            return super.getConnection();
        }
        try
        {
            return dataSource.getConnection();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return super.getConnection();
        }
    }
    
    public boolean isVaild()
    {
        return (dataSource == null ? false : true);
    }
    
    public DataSource getDataSource()
    {
        return dataSource;
    }
    
    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }
    
}
