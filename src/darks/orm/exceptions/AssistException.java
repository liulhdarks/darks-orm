/**
 * 类名:LogException.java
 * 作者:刘力华
 * 创建时间:2012-5-3
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class AssistException extends RuntimeException
{
    
    private static final long serialVersionUID = -8637688951245687945L;
    
    public AssistException()
    {
    }
    
    public AssistException(String message)
    {
        super(message);
    }
    
    public AssistException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public AssistException(Throwable cause)
    {
        super(cause);
    }
}
