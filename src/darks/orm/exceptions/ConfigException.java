/**
 * 类名:ConfigException.java
 * 作者:刘力华
 * 创建时间:2012-5-13
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.exceptions;

public class ConfigException extends AssistException
{
    
    private static final long serialVersionUID = -5386924734523961152L;
    
    public ConfigException()
    {
        super();
    }
    
    public ConfigException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public ConfigException(String message)
    {
        super(message);
    }
    
    public ConfigException(Throwable cause)
    {
        super(cause);
    }
    
}
