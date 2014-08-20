/**
 * 类名:SqlMapExecutorAdapter.java
 * 作者:刘力华
 * 创建时间:2012-6-21
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

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
        // 前置
        boolean isNext = beforeInvoke();
        Object result = null;
        if (!isNext)
        {
            return null;
        }
        result = bodyInvoke();
        // 后置
        isNext = afterInvoke();
        if (!isNext)
            return null;
        return result;
    }
    
}
