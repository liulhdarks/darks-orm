

package darks.orm.exceptions;

public class ClassReflectException extends SessionException
{
    
    private static final long serialVersionUID = 7451647047467286499L;
    
    public ClassReflectException()
    {
    }
    
    public ClassReflectException(String message)
    {
        super(message);
    }
    
    public ClassReflectException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public ClassReflectException(Throwable cause)
    {
        super(cause);
    }
}
