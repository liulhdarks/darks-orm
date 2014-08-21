/**
 * ����:SessionException.java
 * ����:������
 * ����ʱ��:2012-5-13
 * �汾:1.0.0 alpha 
 * ��Ȩ:CopyRight(c)2012 ������  ����Ŀ��������Ȩ������������  
 * ����:
 */

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