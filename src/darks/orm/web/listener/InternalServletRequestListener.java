package darks.orm.web.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import darks.orm.web.context.InternalRequestHolder;
import darks.orm.web.context.RequestContext;
import darks.orm.web.context.RequestContext.RequestHolderType;

public class InternalServletRequestListener implements ServletRequestListener
{
    
    public InternalServletRequestListener()
    {
        RequestContext.getInstance().setRequestHolder(RequestHolderType.Internal);
    }
    
    @Override
    public void requestDestroyed(ServletRequestEvent arg0)
    {
        InternalRequestHolder.getInstance().destory();
        
    }
    
    @Override
    public void requestInitialized(ServletRequestEvent requestEvent)
    {
        if (!(requestEvent.getServletRequest() instanceof HttpServletRequest))
        {
            throw new IllegalArgumentException("Request is not an HttpServletRequest: "
                + requestEvent.getServletRequest());
        }
        HttpServletRequest request = (HttpServletRequest)requestEvent.getServletRequest();
        InternalRequestHolder.getInstance().setRequest(request);
    }
    
}
