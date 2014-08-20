/**
 * 类名:SqlMapUpdateException.java
 * 作者:刘力华
 * 创建时间:2012-5-13
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class SqlMapUpdateException extends UpdateException
{
    
    private static final long serialVersionUID = -6594243214035691748L;
    
    public SqlMapUpdateException()
    {
        super();
    }
    
    public SqlMapUpdateException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public SqlMapUpdateException(String message)
    {
        super(message);
    }
    
    public SqlMapUpdateException(Throwable cause)
    {
        super(cause);
    }
    
}
