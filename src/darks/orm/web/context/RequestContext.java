package darks.orm.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import darks.orm.util.PluginHelper;

public class RequestContext
{
    
    public enum RequestHolderType
    {
        None, Internal, Spring
    }
    
    public static RequestContext instance = null;
    
    private RequestHolder requestHolder = null;
    
    private RequestHolderType requestHolderType = RequestHolderType.None;
    
    public static RequestContext getInstance()
    {
        if (instance == null)
        {
            instance = new RequestContext();
        }
        return instance;
    }
    
    protected RequestContext()
    {
        if (PluginHelper.isRegSpringRequestPlugin())
        {
            requestHolderType = RequestHolderType.Spring;
        }
    }
    
    public HttpServletRequest getRequest()
    {
        RequestHolder holder = getRequestHolder();
        if (holder == null)
            return null;
        HttpServletRequest request = holder.getRequest();
        if (request == null)
        {
            requestHolderType = RequestHolderType.None;
        }
        return request;
    }
    
    public HttpSession getSession()
    {
        RequestHolder holder = getRequestHolder();
        if (holder == null)
            return null;
        HttpSession session = holder.getSession();
        if (session == null)
        {
            requestHolderType = RequestHolderType.None;
        }
        return session;
    }
    
    private RequestHolder getRequestHolder()
    {
        if (requestHolder != null)
            return requestHolder;
        if (requestHolderType == RequestHolderType.Internal)
        {
            requestHolder = InternalRequestHolder.getInstance();
        }
        else if (requestHolderType == RequestHolderType.Spring)
        {
            requestHolder = new SpringRequestHolder();
        }
        else
        {
            return null;
        }
        return requestHolder;
    }
    
    public void setRequestHolder(RequestHolderType requestHolderType)
    {
        this.requestHolderType = requestHolderType;
    }
    
    public RequestHolderType getRequestHolderType()
    {
        if (requestHolderType != RequestHolderType.None)
            getRequest();
        return requestHolderType;
    }
    
    public boolean isVaild()
    {
        return (requestHolderType != RequestHolderType.None);
    }
}
