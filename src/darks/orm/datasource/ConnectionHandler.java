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