/**
 * 
 * Copyright 2014 The Darks ORM Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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