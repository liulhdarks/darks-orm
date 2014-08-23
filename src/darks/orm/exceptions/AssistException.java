

package darks.orm.exceptions;

public class AssistException extends RuntimeException
{
    
    private static final long serialVersionUID = -8637688951245687945L;
    
    public AssistException()
    {
    }
    
    public AssistException(String message)
    {
        super(message);
    }
    
    public AssistException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public AssistException(Throwable cause)
    {
        super(cause);
    }
}
