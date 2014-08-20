package darks.orm.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import darks.orm.core.session.SessionContext;

public class InternalServletContentListener implements ServletContextListener
{
    
    @Override
    public void contextDestroyed(ServletContextEvent arg0)
    {
        try
        {
            SessionContext.destroy();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void contextInitialized(ServletContextEvent arg0)
    {
        try
        {
            SessionContext.build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
