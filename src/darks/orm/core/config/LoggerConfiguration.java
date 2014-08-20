/**
 * 类名:LoggerConfiguration.java
 * 作者:刘力华
 * 创建时间:2012-5-29
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

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
