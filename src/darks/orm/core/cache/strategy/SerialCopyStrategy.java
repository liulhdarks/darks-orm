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

import darks.orm.core.cache.CacheKey;
import darks.orm.core.cache.CacheObject;
import darks.orm.util.ByteHelper;

public class SerialCopyStrategy implements CopyStrategy
{
    
    private static final long serialVersionUID = 6419864241489980487L;
    
    /**
     * �����
     * 
     * @param cacheObject �������
     * @param key ��ֵ
     * @param value ֵ
     * @return
     * @throws Exception
     */
    @Override
    public Object read(CacheObject cacheObject, CacheKey key, Object value)
        throws Exception
    {
        if (value == null)
            return null;
        byte[] bytes = (byte[])value;
        return ByteHelper.ByteToObject(bytes);
    }
    
    /**
     * ����д
     * 
     * @param cacheObject �������
     * @param key ��ֵ
     * @param value ֵ
     * @return
     * @throws Exception
     */
    @Override
    public Object write(CacheObject cacheObject, CacheKey key, Object value)
        throws Exception
    {
        byte[] bytes = ByteHelper.ObjectToByte(value);
        cacheObject.setType(0);
        return bytes;
    }
    
}