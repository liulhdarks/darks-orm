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

package darks.orm.core.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import darks.orm.core.config.sqlmap.SqlMapConfiguration;
import darks.orm.datasource.factory.DataSourceFactory;
import darks.orm.exceptions.ConfigException;

public class Configuration
{
    
    private static final int SQLMAP_INIT_SIZE = 8;
    
    private Map<String, DataSourceConfiguration> dsConfigs = new HashMap<String, DataSourceConfiguration>();
    
    private DataSourceConfiguration mainDsConfig;
    
    private CacheConfiguration cacheConfig = new CacheConfiguration();
    
    private EntityConfiguration entityConfig = new EntityConfiguration();
    
    private SqlMapConfiguration sqlMapConfig = new SqlMapConfiguration();
    
    private List<String> sqlMapsPath = new ArrayList<String>(SQLMAP_INIT_SIZE);
    
    private boolean useThreadLocal = true;
    
    public Configuration()
    {
        
    }
    
    public void addDataSourceConfig(String key, DataSourceConfiguration dsConfig, boolean isMain)
    {
        dsConfigs.put(key, dsConfig);
        if (isMain)
            mainDsConfig = dsConfig;
    }
    
    public DataSourceConfiguration getDataSourceConfig(String key)
    {
        return dsConfigs.get(key);
    }
    
    // 确定主数据源
    public void ensureMainDataSourceConfig()
    {
        if (mainDsConfig == null && !dsConfigs.isEmpty())
        {
            for (DataSourceConfiguration ds : dsConfigs.values())
            {
                mainDsConfig = ds;
                break;
            }
        }
        
        if (DataSourceFactory.getInstance().isVaild())
        {
        	DataSourceConfiguration ds = mainDsConfig;
        	mainDsConfig = DataSourceFactory.getInstance().getDataSourceConfig();
        	if (ds != null)
        	{
            	mainDsConfig.setNext(ds);
            	mainDsConfig.setNextId(ds.getId());
        	}
        }
        if (mainDsConfig == null)
        {
            throw new ConfigException("Data source configuration has no main data source node.");
        }
    }
    
    // 确定数据源链
    public void ensureDataSourceChain()
        throws ConfigException
    {
        if (!dsConfigs.isEmpty())
        {
            for (DataSourceConfiguration dsc : dsConfigs.values())
            {
                String nextId = dsc.getNextId();
                if (nextId == null)
                    continue;
                DataSourceConfiguration ref = getDataSourceConfig(nextId);
                if (ref == null)
                    throw new ConfigException("DataSource id from chain named '" + nextId + "' does not exists");
                dsc.setNext(ref);
            }
        }
    }
    
    public Collection<DataSourceConfiguration> getDataSourceConfigs()
    {
        return dsConfigs.values();
    }
    
    public DataSourceConfiguration getMainDataSourceConfig()
    {
        return mainDsConfig;
    }
    
    public boolean isUseThreadLocal()
    {
        return useThreadLocal;
    }
    
    public void setUseThreadLocal(boolean useThreadLocal)
    {
        this.useThreadLocal = useThreadLocal;
    }
    
    public CacheConfiguration getCacheConfig()
    {
        return cacheConfig;
    }
    
    public EntityConfiguration getEntityConfig()
    {
        return entityConfig;
    }
    
    public List<String> getSqlMapsPath()
    {
        return sqlMapsPath;
    }
    
    public SqlMapConfiguration getSqlMapConfig()
    {
        return sqlMapConfig;
    }
    
}