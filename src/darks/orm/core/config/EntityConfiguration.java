/**
 * 类名:EntityConfiguration.java
 * 作者:刘力华
 * 创建时间:2012-5-29
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
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
        return entityMap.get(key);
    }
}
