
package darks.orm.log.impl;

import darks.orm.log.Logger;

public class Log4jLogger implements Logger
{
    private org.apache.log4j.Logger log;
    
    public Log4jLogger(String clazz)
    {
        this.log = org.apache.log4j.Logger.getLogger(clazz);
    }
    
    public boolean isDebugEnabled()
    {
        return this.log.isDebugEnabled();
    }
    
    public void error(String s, Throwable e)
    {
        this.log.error(s, e);
    }
    
    public void error(String s)
    {
        this.log.error(s);
    }
    
    public void debug(String s)
    {
        this.log.debug(s);
    }
    
    public void debug(String s, Throwable e)
    {
        this.log.debug(s, e);
    }
    
    public void warn(String s)
    {
        this.log.warn(s);
    }
}
