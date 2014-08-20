/**
 * 类名:CacheException.java
 * 作者:刘力华
 * 创建时间:2012-5-13
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class CacheException extends AssistException
{
    
    private static final long serialVersionUID = 4443735064893844302L;
    
    public CacheException()
    {
        super();
    }
    
    public CacheException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public CacheException(String message)
    {
        super(message);
    }
    
    public CacheException(Throwable cause)
    {
        super(cause);
    }
    
}
