

package darks.orm.exceptions;

public class TransformException extends SessionException
{
    
    private static final long serialVersionUID = -2988528111588417451L;
    
    public TransformException()
    {
        super();
    }
    
    public TransformException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public TransformException(String message)
    {
        super(message);
    }
    
    public TransformException(Throwable cause)
    {
        super(cause);
    }
    
}
