package darks.orm.log.impl;

import darks.orm.log.Logger;

public class StdIOLogger implements Logger
{
    
    public StdIOLogger(String clazz)
    {
        
    }
    
    public boolean isDebugEnabled()
    {
        return true;
    }
    
    public void error(String s, Throwable e)
    {
        System.err.println(s);
        e.printStackTrace(System.err);
    }
    
    public void error(String s)
    {
        System.err.println(s);
    }
    
    public void debug(String s)
    {
        System.err.println(s);
    }
    
    public void debug(String s, Throwable e)
    {
        System.err.println(s);
        e.printStackTrace(System.err);
    }
    
    public void warn(String s)
    {
        System.err.println(s);
    }
}
