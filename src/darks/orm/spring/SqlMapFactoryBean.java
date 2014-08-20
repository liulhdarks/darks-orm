/**
 * 类名:SqlMapFactoryBean.java
 * 作者:刘力华
 * 创建时间:上午01:41:02
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.spring;

import darks.orm.app.SqlSession;
import org.springframework.beans.factory.FactoryBean;

public class SqlMapFactoryBean<T> extends SqlMapSessionSupport implements FactoryBean<T>
{
    
    private Class<T> sqlMapInterface;
    
    public SqlMapFactoryBean()
    {
        
    }
    
    @Override
    public T getObject()
        throws Exception
    {
        SqlSession session = getSession();
        if (session != null && sqlMapInterface != null)
        {
            return session.getSqlMap(sqlMapInterface);
        }
        return null;
    }
    
    @Override
    public Class<?> getObjectType()
    {
        return sqlMapInterface;
    }
    
    @Override
    public boolean isSingleton()
    {
        return false;
    }
    
    public void close()
    {
        SqlSession session = getSession();
        if (session != null)
        {
            session.close();
        }
    }
    
    public Class<T> getSqlMapInterface()
    {
        return sqlMapInterface;
    }
    
    public void setSqlMapInterface(Class<T> sqlMapInterface)
    {
        this.sqlMapInterface = sqlMapInterface;
    }
    
}
