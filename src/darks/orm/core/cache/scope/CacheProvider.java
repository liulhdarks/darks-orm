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
