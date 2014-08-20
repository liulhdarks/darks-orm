package darks.orm.core.data.xml;

public class DMLUpdateData
{
    
    private String sql;
    
    private AspectData aspectData;
    
    public DMLUpdateData()
    {
        
    }
    
    public DMLUpdateData(String sql)
    {
        super();
        this.sql = sql;
    }
    
    public String getSql()
    {
        return sql;
    }
    
    public void setSql(String sql)
    {
        this.sql = sql;
    }
    
    public AspectData getAspectData()
    {
        return aspectData;
    }
    
    public void setAspectData(AspectData aspectData)
    {
        this.aspectData = aspectData;
    }
    
}
