package darks.orm.core.cache;

import java.util.concurrent.ConcurrentMap;

import darks.orm.core.data.EntityData;
import darks.orm.core.data.FieldData;
import darks.orm.core.data.FieldData.FieldFlag;

public class CacheObjectUpdater
{
    
    public static void update(CacheKey key, Object oldcache, Object newcache)
    {
        update(key, (CacheObject)oldcache, (CacheObject)newcache);
    }
    
    /**
     * 更新缓存实体外键值
     * 
     * @param key 键值
     * @param oldcache 久缓存实例
     * @param newcache 新缓存实例
     */
    public static void update(CacheKey key, CacheObject oldcache, CacheObject newcache)
    {
        try
        {
            Object oldobj = oldcache.getObject();
            Object newobj = newcache.getObject();
            EntityData data = key.getData();
            ConcurrentMap<String, FieldData> map = data.getMapNameFields();
            for (FieldData fdata : map.values())
            {
                // System.out.println(fdata.getFieldName()+" "+fdata.getFieldType());
                if (fdata.getFieldFlag() == FieldFlag.FkEntity)
                {
                    Object foldobj = fdata.getValue(oldobj);
                    Object fnewobj = fdata.getValue(newobj);
                    if (fnewobj == null)
                        continue;
                    if (foldobj == null)
                    {
                        fdata.setValue(oldobj, fnewobj);
                    }
                    else
                    {
                        EntityData fedata = fdata.getFkData();
                        ConcurrentMap<String, FieldData> fmap = fedata.getMapNameFields();
                        for (FieldData fkdata : fmap.values())
                        {
                            Object fkoldobj = fkdata.getValue(oldobj);
                            Object fknewobj = fkdata.getValue(newobj);
                            if (fkoldobj == null || !fkoldobj.equals(fknewobj))
                            {
                                fkdata.setValue(oldobj, fknewobj);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
