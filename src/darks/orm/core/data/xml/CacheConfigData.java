package darks.orm.core.data.xml;

import darks.orm.core.cache.control.CacheControlStrategy;
import darks.orm.core.cache.scope.CacheScopeType;
import darks.orm.core.cache.strategy.CopyStrategy;
import darks.orm.core.cache.strategy.FieldCopyStrategy;
import darks.orm.core.cache.strategy.RefCopyStrategy;
import darks.orm.core.cache.strategy.SerialCopyStrategy;
import darks.orm.core.config.CacheConfiguration.CopyStrategyType;

public class CacheConfigData
{
    
    private String id;
    
    private CacheScopeType cacheEnumType;
    
    private CacheControlStrategy cacheStrategy = CacheControlStrategy.Lru;
    
    private int maxObject = 10000;
    
    private boolean eternal = true;
    
    private int idleTime = 0;
    
    private int liveTime = 0;
    
    private boolean overflowToDisk = false;
    
    private boolean entirety = true;
    
    private CopyStrategyType copyStrategyType = CopyStrategyType.Field;
    
    private String ehcacheConfigPath = null;
    
    private int maxElementsOnDisk = 1000000;
    
    private boolean diskPersistent = false;
    
    private int diskExpiryThreadIntervalSeconds = 120;
    
    private String memoryStoreEvictionPolicy = "LRU";
    
    public CacheConfigData()
    {
        
    }
    
    public CacheConfigData(CacheScopeType cacheEnumType, CacheControlStrategy cacheStrategy, int maxObject,
        boolean eternal, int idleTime, int liveTime)
    {
        super();
        this.cacheEnumType = cacheEnumType;
        this.cacheStrategy = cacheStrategy;
        this.maxObject = maxObject;
        this.eternal = eternal;
        this.idleTime = idleTime;
        this.liveTime = liveTime;
    }
    
    public CacheConfigData(String id, CacheScopeType cacheEnumType, int maxObject, boolean eternal, int idleTime,
        int liveTime, boolean overflowToDisk)
    {
        super();
        this.id = id;
        this.cacheEnumType = cacheEnumType;
        this.maxObject = maxObject;
        this.eternal = eternal;
        this.idleTime = idleTime;
        this.liveTime = liveTime;
        this.overflowToDisk = overflowToDisk;
    }
    
    public CacheConfigData(String id, CacheScopeType cacheEnumType, CacheControlStrategy cacheStrategy, int maxObject,
        boolean eternal, int idleTime, int liveTime, boolean overflowToDisk)
    {
        super();
        this.id = id;
        this.cacheEnumType = cacheEnumType;
        this.cacheStrategy = cacheStrategy;
        this.maxObject = maxObject;
        this.eternal = eternal;
        this.idleTime = idleTime;
        this.liveTime = liveTime;
        this.overflowToDisk = overflowToDisk;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public CacheScopeType getCacheEnumType()
    {
        return cacheEnumType;
    }
    
    public void setCacheEnumType(CacheScopeType cacheEnumType)
    {
        this.cacheEnumType = cacheEnumType;
    }
    
    public CacheControlStrategy getCacheStrategy()
    {
        return cacheStrategy;
    }
    
    public void setCacheStrategy(CacheControlStrategy cacheStrategy)
    {
        this.cacheStrategy = cacheStrategy;
    }
    
    public int getMaxObject()
    {
        return maxObject;
    }
    
    public void setMaxObject(int maxObject)
    {
        this.maxObject = maxObject;
    }
    
    public boolean isEternal()
    {
        return eternal;
    }
    
    public void setEternal(boolean eternal)
    {
        this.eternal = eternal;
    }
    
    public int getIdleTime()
    {
        return idleTime;
    }
    
    public void setIdleTime(int idleTime)
    {
        this.idleTime = idleTime;
    }
    
    public int getLiveTime()
    {
        return liveTime;
    }
    
    public void setLiveTime(int liveTime)
    {
        this.liveTime = liveTime;
    }
    
    public boolean isOverflowToDisk()
    {
        return overflowToDisk;
    }
    
    public void setOverflowToDisk(boolean overflowToDisk)
    {
        this.overflowToDisk = overflowToDisk;
    }
    
    public boolean isEntirety()
    {
        return entirety;
    }
    
    public void setEntirety(boolean entirety)
    {
        this.entirety = entirety;
    }
    
    public String getEhcacheConfigPath()
    {
        return ehcacheConfigPath;
    }
    
    public void setEhcacheConfigPath(String ehcacheConfigPath)
    {
        this.ehcacheConfigPath = ehcacheConfigPath;
    }
    
    public int getMaxElementsOnDisk()
    {
        return maxElementsOnDisk;
    }
    
    public void setMaxElementsOnDisk(int maxElementsOnDisk)
    {
        this.maxElementsOnDisk = maxElementsOnDisk;
    }
    
    public boolean isDiskPersistent()
    {
        return diskPersistent;
    }
    
    public void setDiskPersistent(boolean diskPersistent)
    {
        this.diskPersistent = diskPersistent;
    }
    
    public int getDiskExpiryThreadIntervalSeconds()
    {
        return diskExpiryThreadIntervalSeconds;
    }
    
    public void setDiskExpiryThreadIntervalSeconds(int diskExpiryThreadIntervalSeconds)
    {
        this.diskExpiryThreadIntervalSeconds = diskExpiryThreadIntervalSeconds;
    }
    
    public String getMemoryStoreEvictionPolicy()
    {
        return memoryStoreEvictionPolicy;
    }
    
    public void setMemoryStoreEvictionPolicy(String memoryStoreEvictionPolicy)
    {
        this.memoryStoreEvictionPolicy = memoryStoreEvictionPolicy;
    }
    
    public CopyStrategyType getCopyStrategyType()
    {
        return copyStrategyType;
    }
    
    public void setCopyStrategyType(CopyStrategyType copyStrategyType)
    {
        this.copyStrategyType = copyStrategyType;
    }
    
    public CopyStrategy getCopyStrategy()
    {
        return getCopyStrategy(copyStrategyType);
    }
    
    public CopyStrategy getCopyStrategy(CopyStrategyType copyStrategyType)
    {
        if (copyStrategyType == CopyStrategyType.Field)
        {
            return new FieldCopyStrategy();
        }
        else if (copyStrategyType == CopyStrategyType.Serial)
        {
            return new SerialCopyStrategy();
        }
        else
        {
            return new RefCopyStrategy();
        }
    }
}
