/**
 * 类名:PssistenceExcetpion.java
 * 作者:刘力华
 * 创建时间:2012-5-13
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class PersistenceException extends SessionException
{
    
    private static final long serialVersionUID = 3090422452271823688L;
    
    public PersistenceException()
    {
        super();
    }
    
    public PersistenceException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public PersistenceException(String message)
    {
        super(message);
    }
    
    public PersistenceException(Throwable cause)
    {
        super(cause);
    }
    
}
