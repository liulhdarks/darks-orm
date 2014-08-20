/**
 * 类名:QueryException.java
 * 作者:刘力华
 * 创建时间:2012-5-13
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:查询异常
 */

package darks.orm.exceptions;

public class QueryException extends AssistException
{
    
    private static final long serialVersionUID = -6175693598200491665L;
    
    public QueryException()
    {
        super();
    }
    
    public QueryException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public QueryException(String message)
    {
        super(message);
    }
    
    public QueryException(Throwable cause)
    {
        super(cause);
    }
    
}
