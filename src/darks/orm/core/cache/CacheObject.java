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

import darks.orm.core.cache.strategy.CopyStrategy;
import darks.orm.core.cache.strategy.FieldCopyStrategy;

public class CacheObject implements Serializable
{
    
    private static final long serialVersionUID = -8485785553022694880L;
    
    private Object obj;
    
    private int objType = 0; // 0 single 1 list
    
    private CacheKey key;
    
    private long lastIdleTime = 0;
    
    private long lastLiveTime = 0;
    
    private CopyStrategy copyStrategy;
    
    public CacheObject()
    {
        lastIdleTime = System.currentTimeMillis();
        lastLiveTime = System.currentTimeMillis();
        copyStrategy = new FieldCopyStrategy();
    }
    
    public CacheObject(CacheKey key, Object obj)
        throws Exception
    {
        this(new FieldCopyStrategy(), key, obj);
    }
    
    public CacheObject(CopyStrategy copyStrategy, CacheKey key, Object obj)
        throws Exception
    {
        lastIdleTime = System.currentTimeMillis();
        lastLiveTime = System.currentTimeMillis();
        this.copyStrategy = copyStrategy;
        this.key = key;
        setObject(obj);
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((obj == null) ? 0 : obj.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CacheObject other = (CacheObject)obj;
        if (key == null)
        {
            if (other.key != null)
                return false;
        }
        else if (!key.equals(other.key))
            return false;
        if (this.obj == null)
        {
            if (other.obj != null)
                return false;
        }
        else if (!this.obj.equals(other.obj))
            return false;
        return true;
    }
    
    public Object getObject()
        throws Exception
    {
        return copyStrategy.read(this, key, obj);
    }
    
    public void setObject(Object obj)
        throws Exception
    {
        this.obj = copyStrategy.write(this, key, obj);
    }
    
    public long getLastIdleTime()
    {
        return lastIdleTime;
    }
    
    public void setLastIdleTime(long lastIdleTime)
    {
        this.lastIdleTime = lastIdleTime;
    }
    
    public long getLastLiveTime()
    {
        return lastLiveTime;
    }
    
    public void setLastLiveTime(long lastLiveTime)
    {
        this.lastLiveTime = lastLiveTime;
    }
    
    public CacheKey getKey()
    {
        return key;
    }
    
    public void setKey(CacheKey key)
    {
        this.key = key;
    }
    
    public int getType()
    {
        return objType;
    }
    
    public void setType(int objType)
    {
        this.objType = objType;
    }
    
    public CopyStrategy getCopyStrategy()
    {
        return copyStrategy;
    }
    
}