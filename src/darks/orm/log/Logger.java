
package darks.orm.log;

public abstract interface Logger
{
    
    public abstract boolean isDebugEnabled();
    
    public abstract void error(String paramString, Throwable paramThrowable);
    
    public abstract void error(String paramString);
    
    public abstract void debug(String paramString);
    
    public abstract void debug(String s, Throwable e);
    
    public abstract void warn(String paramString);
}
