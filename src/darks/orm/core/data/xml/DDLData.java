package darks.orm.core.data.xml;

public class DDLData
{
    
    public enum DDLType
    {
        Create, Alter
    };
    
    private String id;
    
    private boolean checkTable = true;
    
    private boolean autoRunable = true;
    
    private String sql;
    
    private DDLType type;
    
    private String tableName;
    
    public DDLData()
    {
        
    }
    
    public DDLData(String id, boolean checkTable, String sql, DDLType type)
    {
        super();
        this.id = id;
        this.checkTable = checkTable;
        this.sql = sql;
        this.type = type;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public boolean isCheckTable()
    {
        return checkTable;
    }
    
    public void setCheckTable(boolean checkTable)
    {
        this.checkTable = checkTable;
    }
    
    public String getSql()
    {
        return sql;
    }
    
    public void setSql(String sql)
    {
        this.sql = sql;
    }
    
    public DDLType getType()
    {
        return type;
    }
    
    public void setType(DDLType type)
    {
        this.type = type;
    }
    
    public boolean isAutoRunable()
    {
        return autoRunable;
    }
    
    public void setAutoRunable(boolean autoRunable)
    {
        this.autoRunable = autoRunable;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
}
