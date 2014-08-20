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

public class BoneCPConnectionFactory extends ConnectionHandler
{
    
    private static final Logger logger = LoggerFactory.getLogger(JndiConnectionFactory.class);
    
    /**
     * 获得BoneCP连接
     * 
     * @return JDBC连接
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Connection getBoneCPConnection()
        throws DataSourceException, SQLException, ClassNotFoundException
    {
        LogHelper.println(this, "[Thread ID:" + Thread.currentThread().getId() + " NAME:"
            + Thread.currentThread().getName() + "]Get BoneCP Connection");
        Connection conn = null;
        DataSourceConfiguration config = this.getDataSourceConfig();
        DataSource ds = config.getDataSource();
        conn = ds.getConnection();
        // if(InternalContext.getConfigure().Logger().isShowLog()){
        // Statistics s=boneCPPool.getStatistics();
        // Log.println("BoneCP Info Created:"+boneCPPool.getTotalCreatedConnections()+
        // " Free:"+boneCPPool.getTotalFree()+
        // " Leased:"+boneCPPool.getTotalLeased()+
        // " StmtPrepared:"+s.getStatementsPrepared()+
        // " StmtCached:"+s.getStatementsCached());
        // }
        return conn;
        
    }
    
    public Connection getConnection()
    {
        try
        {
            Connection conn = getBoneCPConnection();
            if (conn == null)
            {
                LogHelper.except(logger,
                    "BoneCPConnectionFactory::getConnection connection is null",
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
