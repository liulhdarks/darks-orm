package darks.orm.core.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class CacheList implements Serializable
{
    
    private static final long serialVersionUID = 4451672483657881504L;
    
    private Map<CacheKey, Object> map;
    
    private List<CacheKey> list;
    
    private int count;
    
    public CacheList()
    {
        
    }
    
    public CacheList(Map<CacheKey, Object> map, List<CacheKey> list)
    {
        super();
        this.map = map;
        this.list = list;
    }
    
    public CacheList(Map<CacheKey, Object> map, List<CacheKey> list, int count)
    {
        super();
        this.map = map;
        this.list = list;
        this.count = count;
    }
    
    public Map<CacheKey, Object> getMap()
    {
        return map;
    }
    
    public void setMap(Map<CacheKey, Object> map)
    {
        this.map = map;
    }
    
    public List<CacheKey> getList()
    {
        return list;
    }
    
    public void setList(List<CacheKey> list)
    {
        this.list = list;
    }
    
    public int getCount()
    {
        return count;
    }
    
    public void setCount(int count)
    {
        this.count = count;
    }
    
}
