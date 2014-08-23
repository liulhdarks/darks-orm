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

package darks.orm.core.data.xml;

public class SimpleAspectWrapper
{
    
    protected String sql;
    
    protected Object[] params;
    
    public SimpleAspectWrapper()
    {
        
    }
    
    public SimpleAspectWrapper(String sql, Object[] params)
    {
        super();
        this.sql = sql;
        this.params = params;
    }
    
    public String getSql()
    {
        return sql;
    }
    
    public void setSql(String sql)
    {
        this.sql = sql;
    }
    
    public Object[] getParams()
    {
        return params;
    }
    
    public void setParams(Object[] params)
    {
        if (params == null)
            params = new Object[0];
        this.params = params;
    }
    
}