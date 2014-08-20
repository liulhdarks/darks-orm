package darks.orm.log.impl;

import darks.orm.log.Logger;

public class NoOpLogger implements Logger
{
    
    public NoOpLogger(String clazz)
    {
    }
    
    public boolean isDebugEnabled()
    {
        return true;
    }
    
    public void error(String s, Throwable e)
    {
        
    }
    
    public void error(String s)
    {
        
    }
    
    public void debug(String s)
    {
        
    }
    
    public void debug(String s, Throwable e)
    {
        
    }
    
    public void warn(String s)
    {
        
    }
}
