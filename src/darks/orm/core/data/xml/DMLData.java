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

import darks.orm.core.data.tags.RootTag;

public class DMLData
{
    
    public enum DMLType
    {
        Query, Update
    };
    
    private String id;
    
    private DMLType type;
    
    private boolean autoCache = true;
    
    private String cacheId;
    
    private DMLQueryData queryData;
    
    private DMLUpdateData updateData;
    
    private RootTag sqlTag;
    
    public DMLData()
    {
        
    }
    
    public DMLData(String id, DMLType type)
    {
        super();
        this.id = id;
        this.type = type;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public DMLType getType()
    {
        return type;
    }
    
    public void setType(DMLType type)
    {
        this.type = type;
    }
    
    public DMLQueryData getQueryData()
    {
        return queryData;
    }
    
    public void setQueryData(DMLQueryData queryData)
    {
        this.queryData = queryData;
    }
    
    public DMLUpdateData getUpdateData()
    {
        return updateData;
    }
    
    public void setUpdateData(DMLUpdateData updateData)
    {
        this.updateData = updateData;
    }
    
    public boolean isAutoCache()
    {
        return autoCache;
    }
    
    public void setAutoCache(boolean autoCache)
    {
        this.autoCache = autoCache;
    }
    
    public String getCacheId()
    {
        return cacheId;
    }
    
    public void setCacheId(String cacheId)
    {
        this.cacheId = cacheId;
    }

	public RootTag getSqlTag()
	{
		return sqlTag;
	}

	public void setSqlTag(RootTag sqlTag)
	{
		this.sqlTag = sqlTag;
	}
    
    
}