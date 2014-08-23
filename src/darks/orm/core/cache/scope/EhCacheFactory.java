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

package darks.orm.core.cache.scope;

import darks.orm.core.cache.CacheKey;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

public class EhCacheFactory implements CacheFactory
{
    
    private EhCacheProvider provider;
    
    private Cache cache;
    
    @SuppressWarnings("unused")
    private EhCacheFactory()
    {
        
    }
    
    public EhCacheFactory(EhCacheProvider provider, Cache cache)
    {
        this.provider = provider;
        this.cache = cache;
    }
    
    @Override
    public void cacheObject(CacheKey key, Object obj)
        throws Exception
    {
        if (key == null || obj == null)
            return;
        EhCacheElement el = new EhCacheElement(key, key.getCount(), obj);
        cache.put(new Element(key, el));
    }
    
    @Override
    public boolean containKey(CacheKey key)
    {
        try
        {
            if (getObject(key) != null)
            {
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void debug()
    {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void flush()
    {
        cache.flush();
    }
    
    @Override
    public Object getObject(CacheKey key)
        throws Exception
    {
        Element el = cache.get(key);
        if (el == null)
            return null;
        EhCacheElement cel = (EhCacheElement)el.getObjectValue();
        key.setCount(cel.getCount());
        return cel.getValue();
    }
    
    public EhCacheProvider getProvider()
    {
        return provider;
    }
    
    public Cache getCache()
    {
        return cache;
    }
    
}