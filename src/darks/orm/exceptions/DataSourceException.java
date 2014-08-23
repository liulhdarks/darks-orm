

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
