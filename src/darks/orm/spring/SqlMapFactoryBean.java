
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
