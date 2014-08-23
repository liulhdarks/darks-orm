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

package darks.orm.core.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CacheList implements Serializable
{
    
    private static final long serialVersionUID = 4451672483657881504L;
    
    private Map<CacheKey, Object> map;
    
    private List<CacheKey> list;
    
    private int count;
    
    public CacheList()
    {
        
    }
    
    public CacheList(Map<CacheKey, Object> map, List<CacheKey> list)
    {
        super();
        this.map = map;
        this.list = list;
    }
    
    public CacheList(Map<CacheKey, Object> map, List<CacheKey> list, int count)
    {
        super();
        this.map = map;
        this.list = list;
        this.count = count;
    }
    
    public Map<CacheKey, Object> getMap()
    {
        return map;
    }
    
    public void setMap(Map<CacheKey, Object> map)
    {
        this.map = map;
    }
    
    public List<CacheKey> getList()
    {
        return list;
    }
    
    public void setList(List<CacheKey> list)
    {
        this.list = list;
    }
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
    {
        this.count = count;
    }
    
}