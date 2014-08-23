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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class BoneCPConfiguration extends JdbcConfiguration
{
    
    private BoneCPConfig boneCPConfig = null;
    
    private BoneCPDataSource ds = null;
    
    private int idleConnectionTestPeriod = 60;
    
    private int idleMaxAge = 240;
    
    private int partitionCount = 3;
    
    private int maxConnectionsPerPartition = 30;
    
    private int minConnectionsPerPartition = 10;
    
    private int acquireIncrement = 5;
    
    private int releaseHelperThreads = 3;
    
    private int statementsCacheSize = 0;
    
    private int statementReleaseHelperThreads = 3;
    
    private volatile boolean isInited = false;
    
    private Lock lock = new ReentrantLock();
    
    public BoneCPConfiguration()
    {
        
    }
    
    public DataSource getDataSource()
        throws ClassNotFoundException
    {
        if (!isInited)
        {
            lock.lock();
            try
            {
                if (!isInited)
                {
                    Class.forName(getDriver());
                    boneCPConfig = new BoneCPConfig();
                    boneCPConfig.setJdbcUrl(getUrl());
                    boneCPConfig.setUsername(getUsername());
                    boneCPConfig.setPassword(getPassword());
                    // 设置每60秒检查数据库中的空闲连接数
                    boneCPConfig.setIdleConnectionTestPeriod(idleConnectionTestPeriod, TimeUnit.MINUTES);
                    // 设置连接空闲时间
                    boneCPConfig.setIdleMaxAge(idleMaxAge, TimeUnit.MINUTES);
                    boneCPConfig.setPartitionCount(partitionCount);
                    // 设置每个分区中的最大连接数
                    boneCPConfig.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
                    // 设置每个分区中的最小连接数
                    boneCPConfig.setMinConnectionsPerPartition(minConnectionsPerPartition);
                    // 当连接池中的连接耗尽的时候 BoneCP一次同时获取的连接数
                    boneCPConfig.setAcquireIncrement(acquireIncrement);
                    // 连接释放处理
                    boneCPConfig.setReleaseHelperThreads(releaseHelperThreads);
                    ds = new BoneCPDataSource(boneCPConfig);
                    isInited = true;
                }
            }
            finally
            {
                lock.unlock();
            }
        }
        return ds;
    }
    
    @Override
    public void destroy()
    {
        lock.lock();
        try
        {
            if (ds != null)
            {
                ds.close();
            }
            isInited = false;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    public boolean isInited()
    {
        return isInited;
    }
    
    public BoneCPConfig getBoneCPConfig()
    {
        return boneCPConfig;
    }
    
    public void setBoneCPConfig(BoneCPConfig boneCPConfig)
    {
        this.boneCPConfig = boneCPConfig;
    }
    
    public int getIdleConnectionTestPeriod()
    {
        return idleConnectionTestPeriod;
    }
    
    public void setIdleConnectionTestPeriod(int idleConnectionTestPeriod)
    {
        this.idleConnectionTestPeriod = idleConnectionTestPeriod;
    }
    
    public int getIdleMaxAge()
    {
        return idleMaxAge;
    }
    
    public void setIdleMaxAge(int idleMaxAge)
    {
        this.idleMaxAge = idleMaxAge;
    }
    
    public int getPartitionCount()
    {
        return partitionCount;
    }
    
    public void setPartitionCount(int partitionCount)
    {
        this.partitionCount = partitionCount;
    }
    
    public int getMaxConnectionsPerPartition()
    {
        return maxConnectionsPerPartition;
    }
    
    public void setMaxConnectionsPerPartition(int maxConnectionsPerPartition)
    {
        this.maxConnectionsPerPartition = maxConnectionsPerPartition;
    }
    
    public int getMinConnectionsPerPartition()
    {
        return minConnectionsPerPartition;
    }
    
    public void setMinConnectionsPerPartition(int minConnectionsPerPartition)
    {
        this.minConnectionsPerPartition = minConnectionsPerPartition;
    }
    
    public int getAcquireIncrement()
    {
        return acquireIncrement;
    }
    
    public void setAcquireIncrement(int acquireIncrement)
    {
        this.acquireIncrement = acquireIncrement;
    }
    
    public int getReleaseHelperThreads()
    {
        return releaseHelperThreads;
    }
    
    public void setReleaseHelperThreads(int releaseHelperThreads)
    {
        this.releaseHelperThreads = releaseHelperThreads;
    }
    
    public int getStatementsCacheSize()
    {
        return statementsCacheSize;
    }
    
    public void setStatementsCacheSize(int statementsCacheSize)
    {
        this.statementsCacheSize = statementsCacheSize;
    }
    
    public int getStatementReleaseHelperThreads()
    {
        return statementReleaseHelperThreads;
    }
    
    public void setStatementReleaseHelperThreads(int statementReleaseHelperThreads)
    {
        this.statementReleaseHelperThreads = statementReleaseHelperThreads;
    }
    
}