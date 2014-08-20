package darks.orm.core.data.xml;

public class SimpleAspectWrapper
{
    
    protected String sql;
    
    protected Object[] params;
    
    public SimpleAspectWrapper()
    {
        
    }
    
    public SimpleAspectWrapper(String sql, Object[] params)
    {
        super();
        this.sql = sql;
        this.params = params;
    }
    
    public String getSql()
    {
        return sql;
    }
    
    public void setSql(String sql)
    {
        this.sql = sql;
    }
    
    public Object[] getParams()
    {
        return params;
    }
    
    public void setParams(Object[] params)
    {
        if (params == null)
            params = new Object[0];
        this.params = params;
    }
    
}
