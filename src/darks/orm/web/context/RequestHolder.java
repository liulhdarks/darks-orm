package darks.orm.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface RequestHolder
{
    
    public HttpServletRequest getRequest();
    
    public HttpSession getSession();
    
}
