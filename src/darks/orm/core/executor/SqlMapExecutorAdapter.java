/**
 * ����:SqlMapExecutorAdapter.java
 * ����:������
 * ����ʱ��:2012-6-21
 * �汾:1.0.0 alpha 
 * ��Ȩ:CopyRight(c)2012 ������  ����Ŀ��������Ȩ������������  
 * ����:
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
        // ǰ��
        boolean isNext = beforeInvoke();
        Object result = null;
        if (!isNext)
        {
            return null;
        }
        result = bodyInvoke();
        // ����
        isNext = afterInvoke();
        if (!isNext)
            return null;
        return result;
    }
    
}