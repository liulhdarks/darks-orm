/**
 * 类名:LogException.java
 * 作者:刘力华
 * 创建时间:2012-5-3
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class LogException extends AssistException
{
    
    private static final long serialVersionUID = -5018489305192232704L;
    
    public LogException()
    {
    }
    
    public LogException(String message)
    {
        super(message);
    }
    
    public LogException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public LogException(Throwable cause)
    {
        super(cause);
    }
}
