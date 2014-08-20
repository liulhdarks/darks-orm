package darks.orm.core.cache.scope;

import java.io.Serializable;

import darks.orm.core.cache.CacheKey;

public class EhCacheElement implements Serializable
{
    
    private static final long serialVersionUID = 762935909068446966L;
    
    private CacheKey key;
    
    private int count;
    
    private Object value;
    
    public EhCacheElement()
    {
        
    }
    
    public EhCacheElement(CacheKey key, int count, Object value)
    {
        super();
        this.key = key;
        this.count = count;
        this.value = value;
    }
    
    public CacheKey getKey()
    {
        return key;
    }
    
    public void setKey(CacheKey key)
    {
        this.key = key;
    }
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
    {
        this.count = count;
    }
    
    public Object getValue()
    {
        return value;
    }
    
    public void setValue(Object value)
    {
        this.value = value;
    }
    
}
