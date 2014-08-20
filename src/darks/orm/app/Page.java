/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package darks.orm.app;

import java.util.List;

/**
 * Data object wrapper type, used for paging the list data and the total number
 * of records for storage.
 * 
 * <p>
 * <h1>Page.java</h1>
 * <p>
 * 
 * For example:
 * 
 * <pre>
 *      Page<T> page = session.queryPageList(sql, ...);
 * </pre>
 * 
 * @param <T> <T>
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public class Page<T>
{
    
    /**
     * The current page list data
     */
    private List<T> list;
    
    /**
     * The total number of records the current query.
     */
    private int count;
    
    /**
     * Default constructor
     */
    public Page()
    {
        
    }
    
    public Page(List<T> list, int count)
    {
        this.list = list;
        this.count = count;
    }
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
    {
        this.count = count;
    }
    
    public List<T> getList()
    {
        return list;
    }
    
    public void setList(List<T> list)
    {
        this.list = list;
    }
    
}
