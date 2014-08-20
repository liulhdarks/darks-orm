package darks.orm.datasource;

import java.sql.Connection;

public interface IConnectionFactory
{
    
    public Connection getConnection();
}
