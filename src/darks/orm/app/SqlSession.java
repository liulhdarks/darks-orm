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

package darks.orm.app;

import java.io.*;
import darks.orm.app.factory.SqlMapFactory;
import darks.orm.core.SqlBeanSession;
import darks.orm.core.SqlJdbcSession;

/**
 * SQL session interface, developers use the framework of the main entrance.Most
 * of the API function on this interface.
 * 
 * <p>
 * <h1>SqlSession.java</h1>
 * <p>
 * 
 * For example;
 * 
 * <pre>
 * SqlSession session = SqlSessionFactory.getSession();
 * </pre>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 * @see darks.orm.core.factory.SqlSessionFactory
 */
public interface SqlSession extends Serializable, SqlJdbcSession, SqlBeanSession
{
    
    /**
     * In the cascaded query expressed in the object itself.
     */
    public static final String SELF = "_SELF";
    
    /**
     * Get SqlMap interface object mapping.
     * 
     * @param c Interface class
     * @return Interface mapping object
     */
    public <T> T getSqlMap(Class<T> c);
    
    /**
     * Get SqlMap generic factory object.
     * 
     * @return SqlMap generic factory object
     * @see darks.orm.app.factory.SqlMapFactory
     */
    public SqlMapFactory getSqlMapFactory();
}