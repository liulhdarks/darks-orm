

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
