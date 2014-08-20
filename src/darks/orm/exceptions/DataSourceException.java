/**
 * 类名:DataSourceExcetpion.java
 * 作者:刘力华
 * 创建时间:2012-5-13
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class DataSourceException extends AssistException
{
    
    private static final long serialVersionUID = -3698158765622001790L;
    
    public DataSourceException()
    {
        super();
    }
    
    public DataSourceException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public DataSourceException(String message)
    {
        super(message);
    }
    
    public DataSourceException(Throwable cause)
    {
        super(cause);
    }
    
}
