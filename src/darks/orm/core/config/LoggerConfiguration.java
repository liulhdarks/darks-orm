
package darks.orm.core.config;

public class LoggerConfiguration
{
    
    private boolean showLog = false;
    
    private boolean showSql = false;
    
    private boolean writeLog = false;
    
    public LoggerConfiguration()
    {
        
    }
    
    public boolean isShowLog()
    {
        return showLog;
    }
    
    public void setShowLog(boolean showLog)
    {
        this.showLog = showLog;
    }
    
    public boolean isShowSql()
    {
        return showSql;
    }
    
    public void setShowSql(boolean showSql)
    {
        this.showSql = showSql;
    }
    
    public boolean isWriteLog()
    {
        return writeLog;
    }
    
    public void setWriteLog(boolean writeLog)
    {
        this.writeLog = writeLog;
    }
    
}
