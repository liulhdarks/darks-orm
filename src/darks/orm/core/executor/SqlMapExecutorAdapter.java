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