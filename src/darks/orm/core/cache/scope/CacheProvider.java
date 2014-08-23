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

import darks.orm.core.cache.CacheContext;

public interface CacheProvider
{
    
    /**
     * 初始化EHCACHE
     */
    public boolean initialize(CacheContext cacheContext);
    
    /**
     * ehcache jar包是否已经包含
     * 
     * @return
     */
    public boolean isClassLoaded();
    
    public void shutdown();
    
    public boolean isInit();
}