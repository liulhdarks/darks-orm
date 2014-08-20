/**
 * 类名:AspectException.java
 * 作者:刘力华
 * 创建时间:2012-6-11
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class AspectException extends AssistException
{
    
    private static final long serialVersionUID = 4841370783020086698L;
    
    public AspectException()
    {
        super();
    }
    
    public AspectException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public AspectException(String message)
    {
        super(message);
    }
    
    public AspectException(Throwable cause)
    {
        super(cause);
    }
    
}
