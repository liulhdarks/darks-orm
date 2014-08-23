

package darks.orm.exceptions;

public class SessionException extends AssistException
{
    
    private static final long serialVersionUID = -2988528111588417451L;
    
    public SessionException()
    {
        super();
    }
    
    public SessionException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public SessionException(String message)
    {
        super(message);
    }
    
    public SessionException(Throwable cause)
    {
        super(cause);
    }
    
}
