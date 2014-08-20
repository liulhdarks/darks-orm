/**
 * 类名:LogException.java
 * 作者:刘力华
 * 创建时间:2012-5-3
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class ClassReflectException extends SessionException
{
    
    private static final long serialVersionUID = 7451647047467286499L;
    
    public ClassReflectException()
    {
    }
    
    public ClassReflectException(String message)
    {
        super(message);
    }
    
    public ClassReflectException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public ClassReflectException(Throwable cause)
    {
        super(cause);
    }
}
