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

import darks.orm.core.cache.CacheContext.CacheKeyType;
import darks.orm.core.data.EntityData;

public class CacheKey implements Serializable
{
    
    private static final long serialVersionUID = -6742310631796811187L;
    
    private int id;
    
    private String sql;
    
    private Object[] params;
    
    private int page = 0;
    
    private int pageSize = 0;
    
    private EntityData data;
    
    private CacheKeyType cacheKeyType;
    
    private int count = 0;
    
    private boolean cascade = false;
    
    private int hash = 0;
    
    private static final int INIT_HASH_VALUE = 2;
    
    private static final int HASH_VALUE_PARAM = 32;
    
    public CacheKey()
    {
        
    }
    
    public CacheKey(EntityData data, int id, CacheKeyType cacheKeyType)
    {
        this.data = data;
        this.id = id;
        this.cacheKeyType = cacheKeyType;
    }
    
    public CacheKey(EntityData data, String sql, Object[] params, CacheKeyType cacheKeyType)
    {
        this.data = data;
        this.sql = sql;
        this.params = params;
        this.cacheKeyType = cacheKeyType;
    }
    
    public CacheKey(String sql, Object[] params, int page, int pageSize, EntityData data, CacheKeyType cacheKeyType,
        int count)
    {
        this.sql = sql;
        this.params = params;
        this.page = page;
        this.pageSize = pageSize;
        this.data = data;
        this.cacheKeyType = cacheKeyType;
        this.count = count;
    }
    
    @Override
    public int hashCode()
    {
        if (hash == 0)
        {
            int result = INIT_HASH_VALUE;
            result = getObjectHashCode(cascade, result);
            result = getObjectHashCode(cacheKeyType, result);
            result = getObjectHashCode(data, result);
            result = getObjectHashCode(id, result);
            result = getObjectHashCode(sql, result);
            result = getObjectHashCode(page, result);
            result = getObjectHashCode(pageSize, result);
            result = getObjectsHashCode(result, params);
            hash = result;
        }
        return hash;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (!(obj instanceof CacheKey))
        {
            return super.equals(obj);
        }
        CacheKey key = (CacheKey)obj;
        if (key.isCascade() != cascade)
            return false;
        if (key.getCacheKeyType() != cacheKeyType)
            return false;
        
        if (key.getData() == null)
        {
            if (data != null)
                return false;
        }
        else
        {
            if (!key.getData().equals(data))
                return false;
        }
        
        if (key.getId() != id)
            return false;
        
        if (key.getSql() == null)
        {
            if (sql != null)
                return false;
        }
        else
        {
            if (!key.getSql().equals(sql))
                return false;
        }
        
        if (key.getParams() == null)
        {
            if (params != null)
                return false;
        }
        else
        {
            Object[] ps = key.getParams();
            if (ps.length != params.length)
                return false;
            for (int i = 0; i < ps.length; i++)
            {
                if (!ps[i].equals(params[i]))
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    private int getObjectHashCode(Object obj, int ret)
    {
        int result = HASH_VALUE_PARAM * ret;
        if (obj == null)
            return result;
        if (obj instanceof Boolean)
        {
            boolean bln = (Boolean)obj;
            result = result + (bln ? 0 : 1);
        }
        else if (obj instanceof Byte || obj instanceof Character || obj instanceof Short || obj instanceof Integer)
        {
            result = result + (Integer)obj;
        }
        else if (obj instanceof Long)
        {
            long along = (Long)obj;
            result = result + (int)(along ^ (along >>> 32));
            ;
        }
        else if (obj instanceof Float)
        {
            float afloat = (Float)obj;
            result = result + Float.floatToIntBits(afloat);
        }
        else if (obj instanceof Double)
        {
            double adouble = (Double)obj;
            long tolong = Double.doubleToLongBits(adouble);
            result = result + (int)(tolong ^ (tolong >>> 32));
        }
        else
        {
            result = result + obj.hashCode();
        }
        return result;
    }
    
    private int getObjectsHashCode(int result, Object[] objs)
    {
        if (objs == null)
            return HASH_VALUE_PARAM * result;
        for (int i = 0; i < objs.length; i++)
        {
            result = getObjectHashCode(objs[i], result);
        }
        return result;
    }
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
        hash = 0;
    }
    
    public String getSql()
    {
        return sql;
    }
    
    public void setSql(String sql)
    {
        this.sql = sql;
        hash = 0;
    }
    
    public Object[] getParams()
    {
        return params;
    }
    
    public void setParams(Object[] params)
    {
        this.params = params;
        hash = 0;
    }
    
    public EntityData getData()
    {
        return data;
    }
    
    public void setData(EntityData data)
    {
        this.data = data;
        hash = 0;
    }
    
    public CacheKeyType getCacheKeyType()
    {
        return cacheKeyType;
    }
    
    public void setCacheKeyType(CacheKeyType cacheKeyType)
    {
        this.cacheKeyType = cacheKeyType;
        hash = 0;
    }
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
    {
        this.count = count;
    }
    
    public int getPage()
    {
        return page;
    }
    
    public void setPage(int page)
    {
        this.page = page;
        hash = 0;
    }
    
    public int getPageSize()
    {
        return pageSize;
    }
    
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
        hash = 0;
    }
    
    public boolean isCascade()
    {
        return cascade;
    }
    
    public void setCascade(boolean cascade)
    {
        this.cascade = cascade;
    }
    
}