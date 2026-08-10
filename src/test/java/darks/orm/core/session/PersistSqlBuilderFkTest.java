package darks.orm.core.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import darks.orm.core.data.EntityData;
import darks.orm.core.factory.ClassFactory;
import darks.orm.core.session.model.FkOwner;
import darks.orm.core.session.model.FkRef;

public class PersistSqlBuilderFkTest
{

    @Test
    public void buildUpdateSqlUsesHiddenFkWhenAssociationNotLoaded()
        throws Exception
    {
        EntityData entityData = ClassFactory.parseClass(FkOwner.class);
        assertTrue("ManyToOne entities must use proxy with hidden fk_* fields", entityData.isUseProxy());

        FkOwner owner = entityData.newInstance();
        owner.setId(Integer.valueOf(7));
        owner.setName("dept");
        // Simulate TransformFactory storing the FK id without hydrating the association.
        Method setHiddenFk = owner.getClass().getMethod("setFk_Ref", Integer.class);
        setHiddenFk.invoke(owner, Integer.valueOf(42));
        // Read the association field directly; calling getRef() would trigger lazy-load interception.
        java.lang.reflect.Field refField = FkOwner.class.getDeclaredField("ref");
        refField.setAccessible(true);
        assertEquals("association stays unloaded", null, refField.get(owner));

        List<Object> built = PersistSqlBuilder.buildUpdateSql(FkOwner.class, owner, "fk_owner", false);
        assertNotNull(built);

        String sql = ((String)built.get(0)).toUpperCase();
        Object[] params = (Object[])built.get(1);

        assertTrue("SQL should update REF_ID: " + sql, sql.contains("REF_ID"));
        assertTrue("params should preserve hidden FK id 42: " + Arrays.toString(params),
            contains(params, Integer.valueOf(42)));
        assertFalse("params must not bind null for unloaded FK: " + Arrays.toString(params),
            containsNullBeforePk(params, Integer.valueOf(7)));
    }

    @Test
    public void buildUpdateSqlPrefersLoadedAssociationPrimaryKey()
        throws Exception
    {
        EntityData entityData = ClassFactory.parseClass(FkOwner.class);
        FkOwner owner = entityData.newInstance();
        owner.setId(Integer.valueOf(3));
        owner.setName("x");

        FkRef ref = new FkRef();
        ref.setId(Integer.valueOf(99));
        owner.setRef(ref);

        Method setHiddenFk = owner.getClass().getMethod("setFk_Ref", Integer.class);
        setHiddenFk.invoke(owner, Integer.valueOf(1));

        List<Object> built = PersistSqlBuilder.buildUpdateSql(FkOwner.class, owner, "fk_owner", false);
        Object[] params = (Object[])built.get(1);
        assertTrue("loaded association PK should win: " + Arrays.toString(params),
            contains(params, Integer.valueOf(99)));
    }

    private static boolean contains(Object[] params, Object expected)
    {
        for (Object param : params)
        {
            if (expected.equals(param))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean containsNullBeforePk(Object[] params, Object pk)
    {
        for (int i = 0; i < params.length - 1; i++)
        {
            if (params[i] == null)
            {
                return true;
            }
        }
        return false;
    }
}
