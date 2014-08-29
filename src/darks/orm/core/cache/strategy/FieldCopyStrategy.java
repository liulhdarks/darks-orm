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

package darks.orm.core.cache.strategy;

import java.util.List;

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheObject;
import darks.orm.util.ByteHelper;

@SuppressWarnings("unchecked")
public class FieldCopyStrategy implements CopyStrategy
{
    
    private static final long serialVersionUID = 2985865563487870555L;
    
    /**
     * 缓存读
     * 
     * @param cacheObject 缓存对象
     * @param key 键值
     * @param value 值
     * @return
     * @throws Exception
     */
    @Override
    public Object read(CacheObject cacheObject, CacheKey key, Object value)
        throws Exception
    {
        if (value == null)
            return null;
        if (cacheObject.getType() == 0)
        {
            byte[] bytes = (byte[])value;
            return ByteHelper.ByteToObject(null, key.getData().getClassOrignal(), bytes);
        }
        else
        {
            byte[][] bytes = (byte[][])value;
            return ByteHelper.ByteToList(null, key.getData().getClassOrignal(), bytes);
        }
    }
    
    /**
     * 缓存写
     * 
     * @param cacheObject 缓存对象
     * @param key 键值
     * @param value 值
     * @return
     * @throws Exception
     */
    @Override
    public Object write(CacheObject cacheObject, CacheKey key, Object value)
        throws Exception
    {
        if (value instanceof List)
        {
            cacheObject.setType(1);
            return ByteHelper.ListToByte(key.getData().getClassOrignal(), (List<?>)value);
        }
        else
        {
            cacheObject.setType(0);
            return ByteHelper.ObjectToByte(key.getData().getClassOrignal(), value);
        }
    }
    
}