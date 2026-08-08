package darks.orm.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import darks.orm.core.data.EntityData;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.session.model.StatusEntity;

public class ByteHelperProxyToOriginalTest
{

    @Test
    public void proxyToOriginalUsesOriginalEntityClass()
        throws Exception
    {
        EntityData data = ClassFactory.parseClass(StatusEntity.class);
        assertNotNull(data);
        assertNotNull(data.getClassOrignal());

        StatusEntity source = new StatusEntity();
        source.setId(Integer.valueOf(3));
        source.setStatus(0);
        source.setScore(Integer.valueOf(0));

        Object original = ByteHelper.ProxyToOriginal(StatusEntity.class, source);
        assertNotNull(original);
        assertEquals(StatusEntity.class, original.getClass());
        assertFalse("ProxyToOriginal must not return a CGLIB proxy class",
            original.getClass().getName().contains("$$"));
        assertTrue(original instanceof StatusEntity);
        assertEquals(Integer.valueOf(3), ((StatusEntity)original).getId());
        assertEquals(0, ((StatusEntity)original).getStatus());
        assertEquals(Integer.valueOf(0), ((StatusEntity)original).getScore());
    }
}
