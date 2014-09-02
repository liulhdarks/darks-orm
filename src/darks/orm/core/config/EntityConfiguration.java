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

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import darks.orm.exceptions.ConfigException;

public class EntityConfiguration
{
    
    private static final int ENTITYMAP_INIT_SIZE = 32;
    
    private static ConcurrentMap<String, Class<?>> entityMap = new ConcurrentHashMap<String, Class<?>>(
        ENTITYMAP_INIT_SIZE);
    
    public EntityConfiguration()
    {
        
    }
    
    /**
     * 添加实体配置
     * 
     * @param alias 实体别名
     * @param className 实体类名
     * @throws ConfigException
     */
    public Class<?> addEntityConfig(String alias, String className)
        throws ConfigException
    {
        Class<?> clazz = null;
        try
        {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            throw new ConfigException(className + " does not exists.", e);
        }
        if (alias == null || "".equals(alias))
        {
            entityMap.put(className, clazz);
        }
        else
        {
            entityMap.put(alias, clazz);
            entityMap.put(alias.toLowerCase(), clazz);
            entityMap.put(className, clazz);
        }
        return clazz;
    }
    
    public Collection<Class<?>> getEntitys()
    {
        return entityMap.values();
    }
    
    public Class<?> getEntity(String key)
    {
    	if (key == null || "".equals(key))
    	{
    		return null;
    	}
    	Class<?> clazz = entityMap.get(key);
    	if (clazz == null)
    	{
    		clazz = entityMap.get(key.toLowerCase());
    	}
    	return clazz;
    }
}