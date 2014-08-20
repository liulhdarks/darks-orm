/**
 * 类名:JythonAspectException.java
 * 作者:刘力华
 * 创建时间:2012-6-11
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class JythonAspectException extends AspectException
{
    
    private static final long serialVersionUID = 9081370822504806691L;
    
    public JythonAspectException()
    {
        super();
    }
    
    public JythonAspectException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public JythonAspectException(String message)
    {
        super(message);
    }
    
    public JythonAspectException(Throwable cause)
    {
        super(cause);
    }
    
}
