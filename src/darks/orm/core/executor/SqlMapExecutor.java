
package darks.orm.core.executor;

import java.sql.SQLException;

public interface SqlMapExecutor
{
    
    public boolean initialize();
    
    public boolean beforeInvoke();
    
    public Object bodyInvoke()
        throws SQLException;
    
    public boolean afterInvoke();
    
    public Object invoke()
        throws SQLException;
}
