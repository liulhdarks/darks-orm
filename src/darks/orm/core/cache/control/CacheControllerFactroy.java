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

package darks.orm.core.cache.control;

import java.util.Map;
import java.util.Queue;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheList;
import darks.orm.core.cache.CacheController;
import darks.orm.core.data.xml.CacheConfigData;

public class CacheControllerFactroy
{
    
    public static CacheController getCacheController(CacheConfigData cacheConfigData, Queue<CacheKey> keysList,
        Map<CacheKey, Object> entityMap, Map<CacheKey, CacheList> listMap)
    {
        CacheControlStrategy strategy = cacheConfigData.getCacheStrategy();
        if (strategy == CacheControlStrategy.Fifo)
        {
            return new FifoCacheController(cacheConfigData, keysList, entityMap, listMap);
        }
        else if (strategy == CacheControlStrategy.SoftRef || strategy == CacheControlStrategy.WeakRef)
        {
            return new RefCacheController(cacheConfigData, keysList, entityMap, listMap);
        }
        else
        {
            return new LruCacheController(cacheConfigData, keysList, entityMap, listMap);
        }
    }
}