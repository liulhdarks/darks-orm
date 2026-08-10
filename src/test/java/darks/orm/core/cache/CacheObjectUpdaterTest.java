package darks.orm.core.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import darks.orm.core.cache.strategy.RefCopyStrategy;
import darks.orm.core.data.EntityData;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.session.model.FkOwner;
import darks.orm.core.session.model.FkRef;
import darks.orm.core.cache.CacheContext.CacheKeyType;

public class CacheObjectUpdaterTest
{

    @Test
    public void updateMergesForeignKeyChildFieldsOntoChildInstances()
        throws Exception
    {
        EntityData data = ClassFactory.parseClass(FkOwner.class);

        FkRef oldRef = new FkRef();
        oldRef.setId(Integer.valueOf(1));
        oldRef.setTitle("old");

        FkOwner oldOwner = new FkOwner();
        oldOwner.setId(Integer.valueOf(10));
        oldOwner.setName("owner");
        oldOwner.setRef(oldRef);

        FkRef newRef = new FkRef();
        newRef.setId(Integer.valueOf(1));
        newRef.setTitle("new");

        FkOwner newOwner = new FkOwner();
        newOwner.setId(Integer.valueOf(10));
        newOwner.setName("owner");
        newOwner.setRef(newRef);

        CacheKey key = new CacheKey(data, 10, CacheKeyType.SingleKey);
        CacheObject oldCache = new CacheObject(new RefCopyStrategy(), key, oldOwner);
        CacheObject newCache = new CacheObject(new RefCopyStrategy(), key, newOwner);

        CacheObjectUpdater.update(key, oldCache, newCache);

        FkOwner merged = (FkOwner)oldCache.getObject();
        assertEquals("FK child title should be merged from the new cache entry", "new", merged.getRef()
            .getTitle());
        assertEquals(Integer.valueOf(1), merged.getRef().getId());
    }
}
