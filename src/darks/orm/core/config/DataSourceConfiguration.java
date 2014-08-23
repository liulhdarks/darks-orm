
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
