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

package darks.orm.core.config;

import javax.sql.DataSource;

public class SpringDataSourceConfiguration extends DataSourceConfiguration
{
    
    private DataSource ds;
    
    public SpringDataSourceConfiguration()
    {
        this.type = "spring";
    }
    
    public SpringDataSourceConfiguration(DataSource ds)
    {
        this.ds = ds;
        this.type = "spring";
    }
    
    public DataSource getDataSource()
        throws ClassNotFoundException
    {
        return ds;
    }
    
    @Override
    public void destroy()
    {
        
    }
    
    
    
}