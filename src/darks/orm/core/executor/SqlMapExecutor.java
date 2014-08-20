/**
 * 类名:SqlMapExecutor.java
 * 作者:刘力华
 * 创建时间:2012-6-21
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

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
