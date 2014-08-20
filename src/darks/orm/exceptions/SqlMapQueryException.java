/**
 * 类名:SqlMapQueryException.java
 * 作者:刘力华
 * 创建时间:2012-5-13
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:sqlmap查询异常
 */

package darks.orm.exceptions;

public class SqlMapQueryException extends QueryException
{
    
    private static final long serialVersionUID = 3828227271511439855L;
    
    public SqlMapQueryException()
    {
        super();
    }
    
    public SqlMapQueryException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public SqlMapQueryException(String message)
    {
        super(message);
    }
    
    public SqlMapQueryException(Throwable cause)
    {
        super(cause);
    }
    
}
