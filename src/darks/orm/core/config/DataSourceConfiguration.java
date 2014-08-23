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

import javax.sql.DataSource;

public abstract class DataSourceConfiguration
{
    
    protected ResultSetConfig resultSetConfig = new ResultSetConfig();
    
    protected int fetchSize = 0;
    
    protected boolean autoCommit;
    
    protected String id;
    
    protected String type;
    
    protected DataSourceConfiguration next;
    
    protected String nextId;
    
    public DataSourceConfiguration()
    {
        
    }
    
    public abstract DataSource getDataSource()
        throws ClassNotFoundException;
    
    public abstract void destroy();
    
    public ResultSetConfig getResultSetConfig()
    {
        return resultSetConfig;
    }
    
    public void setResultSetConfig(ResultSetConfig resultSetConfig)
    {
        this.resultSetConfig = resultSetConfig;
    }
    
    public int getFetchSize()
    {
        return fetchSize;
    }
    
    public void setFetchSize(int fetchSize)
    {
        this.fetchSize = fetchSize;
    }
    
    public boolean isAutoCommit()
    {
        return autoCommit;
    }
    
    public void setAutoCommit(boolean autoCommit)
    {
        this.autoCommit = autoCommit;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public DataSourceConfiguration getNext()
    {
        return next;
    }
    
    public void setNext(DataSourceConfiguration next)
    {
        this.next = next;
    }
    
    public String getNextId()
    {
        return nextId;
    }
    
    public void setNextId(String nextId)
    {
        this.nextId = nextId;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
}