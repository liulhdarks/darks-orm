package darks.orm.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class InternalRequestHolder implements RequestHolder
{
    
    public static final ThreadLocal<ServletRequestAttributes> requestThreadLocal =
        new ThreadLocal<ServletRequestAttributes>();
    
    public static InternalRequestHolder instance = null;
    
    public static InternalRequestHolder getInstance()
    {
        if (instance == null)
        {
            instance = new InternalRequestHolder();
        }
        return instance;
    }
    
    private InternalRequestHolder()
    {
        
    }
    
    @Override
    public HttpServletRequest getRequest()
    {
        ServletRequestAttributes attributes = requestThreadLocal.get();
        if (attributes == null)
            return null;
        return attributes.getRequest();
    }
    
    @Override
    public HttpSession getSession()
    {
        ServletRequestAttributes attributes = requestThreadLocal.get();
        if (attributes == null)
            return null;
        return attributes.getSession(true);
    }
    
    public void setRequest(HttpServletRequest request)
    {
        if (request == null)
            return;
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        requestThreadLocal.set(attributes);
    }
    
    public void destory()
    {
        ServletRequestAttributes attributes = requestThreadLocal.get();
        if (attributes != null)
        {
            attributes.requestCompleted();
            requestThreadLocal.set(null);
        }
    }
    
    public void reset()
    {
        requestThreadLocal.set(null);
    }
}
