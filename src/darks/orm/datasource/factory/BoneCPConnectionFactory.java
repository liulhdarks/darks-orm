/**
 * 
 * Copyright 2014 The Darks ORM Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package darks.orm.datasource.factory;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import darks.orm.core.config.DataSourceConfiguration;
import darks.orm.datasource.ConnectionHandler;
import darks.orm.exceptions.DataSourceException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

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
        logger.debug("Get BoneCP Connection");
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