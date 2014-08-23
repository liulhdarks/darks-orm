
package darks.orm.core.executor;

import java.sql.SQLException;

public abstract class SqlMapExecutorAdapter implements SqlMapExecutor
{
    
    public SqlMapExecutorAdapter()
    {
        
    }
    
    @Override
    public boolean initialize()
    {
        return true;
    }
    
    @Override
    public boolean afterInvoke()
    {
        return true;
    }
    
    @Override
    public boolean beforeInvoke()
    {
        return true;
    }
    
    @Override
    public Object invoke()
        throws SQLException
    {
        // «∞÷√
        boolean isNext = beforeInvoke();
        Object result = null;
        if (!isNext)
        {
            return null;
        }
        result = bodyInvoke();
        // ∫Û÷√
        isNext = afterInvoke();
        if (!isNext)
            return null;
        return result;
    }
    
}
