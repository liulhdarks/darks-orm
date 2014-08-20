/**
 * 类名:Logger.java
 * 作者:刘力华
 * 创建时间:2012-5-3
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

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
