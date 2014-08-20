/**
 * 类名:SqlMapSessionSupport.java
 * 作者:刘力华
 * 创建时间:上午01:55:23
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.spring;

import darks.orm.app.SqlSession;
import darks.orm.core.factory.SqlSessionFactory;

public abstract class SqlMapSessionSupport
{
    
    private SqlSession session;
    
    public SqlMapSessionSupport()
    {
        try
        {
            session = SqlSessionFactory.getSession();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public SqlSession getSession()
    {
        return session;
    }
}
