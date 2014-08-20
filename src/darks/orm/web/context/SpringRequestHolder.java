package darks.orm.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class SpringRequestHolder implements RequestHolder
{
    
    public static final String RequestKey = "request";
    
    public static final String SessionKey = "session";
    
    @Override
    public HttpServletRequest getRequest()
    {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        return (HttpServletRequest)attributes.resolveReference(RequestKey);
    }
    
    @Override
    public HttpSession getSession()
    {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        return (HttpSession)attributes.resolveReference(SessionKey);
    }
    
}
