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

package darks.orm.core;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import darks.orm.datasource.factory.StatementFactory.StatementType;

/**
 * SqlJdbcSession interface
 * 
 * <p>
 * <h1>SqlJdbcSession.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public interface SqlJdbcSession extends SqlBaseSession
{
    
    /**
     * Query dataset fields
     * 
     * @param sql SQL statement
     * @return result
     */
    public int executeField(String sql, Object... params);
    
    /**
     * Execute query operation
     * 
     * @param sql SQL statement
     * @param param Injection parameters
     * @return ResultSet object
     */
    public ResultSet executeQuery(String sql, Object... params);
    
    /**
     * Execute query operation
     * 
     * @param sql SQL statement
     * @param param Injection parameters
     * @param stateType Statement Type
     * @return ResultSet object
     */
    public ResultSet executeQuery(String sql, Object param[], StatementType stateType);
    
    /**
     * Obtain CallableStatement object
     * 
     * @param sql SQL statement
     * @return CallableStatement object
     */
    public CallableStatement prepareCall(String sql);
    
    /**
     * Execute Jdbc update
     * 
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Effect of the number of records
     */
    public int executeUpdate(String sql, Object... params);
    
    /**
     * Execute Jdbc update, and returns the primary key value
     * 
     * @param sql SQL statement
     * @param param Injection parameters
     * @return Primary key value
     */
    public Object executeUpdateGeneratedKey(String sql, Object... params);
    
}