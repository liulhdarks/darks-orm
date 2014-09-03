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

import javax.sql.DataSource;

import darks.orm.core.config.DataSourceConfiguration;
import darks.orm.core.config.SpringDataParamConfig;
import darks.orm.core.config.SpringDataSourceConfiguration;
import darks.orm.datasource.ConnectionHandler;

public class DataSourceFactory extends ConnectionHandler
{
    
    private DataSource dataSource;
    
    private DataSourceConfiguration dataSourceConfig;
    
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
    	setDataSource(dataSource);
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
        dataSourceConfig = new SpringDataSourceConfiguration(dataSource);
    }
    
    public void setDataSource(DataSource dataSource, SpringDataParamConfig dataParamConfig)
    {
        this.dataSource = dataSource;
        dataSourceConfig = new SpringDataSourceConfiguration(dataSource);
        dataSourceConfig.getResultSetConfig().setConcurrency(dataParamConfig.getConcurrency());
        dataSourceConfig.getResultSetConfig().setSensitive(dataParamConfig.getSensitive());
        dataSourceConfig.getResultSetConfig().setType(dataParamConfig.getResultSetType());
        dataSourceConfig.setAutoCommit(dataParamConfig.isAutoCommit());
    }

	public DataSourceConfiguration getDataSourceConfig()
	{
		return dataSourceConfig;
	}

	public void setDataSourceConfig(DataSourceConfiguration dataSourceConfig)
	{
		this.dataSourceConfig = dataSourceConfig;
	}
    
}