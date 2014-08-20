/**
 * 类名:SessionException.java
 * 作者:刘力华
 * 创建时间:2012-5-13
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class TransformException extends SessionException
{
    
    private static final long serialVersionUID = -2988528111588417451L;
    
    public TransformException()
    {
        super();
    }
    
    public TransformException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public TransformException(String message)
    {
        super(message);
    }
    
    public TransformException(Throwable cause)
    {
        super(cause);
    }
    
}
