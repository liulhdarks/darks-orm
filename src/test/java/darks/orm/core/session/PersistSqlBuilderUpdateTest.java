package darks.orm.core.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import darks.orm.core.session.model.StatusEntity;

public class PersistSqlBuilderUpdateTest
{

    @Test
    public void buildUpdateSqlIncludesZeroNumericValues()
        throws Exception
    {
        StatusEntity entity = new StatusEntity();
        entity.setId(Integer.valueOf(7));
        entity.setStatus(0);
        entity.setScore(Integer.valueOf(0));
        entity.setNote("ok");

        List<Object> built = PersistSqlBuilder.buildUpdateSql(StatusEntity.class, entity, "status_entity", false);
        assertNotNull("update SQL should be generated for zero-valued fields", built);

        String sql = (String)built.get(0);
        Object[] params = (Object[])built.get(1);

        assertTrue("SQL should update STATUS: " + sql, sql.toUpperCase().contains("STATUS"));
        assertTrue("SQL should update SCORE: " + sql, sql.toUpperCase().contains("SCORE"));
        assertTrue("params should contain status=0: " + Arrays.toString(params),
            containsZero(params));
        assertEquals(Integer.valueOf(7), params[params.length - 1]);
    }

    private static boolean containsZero(Object[] params)
    {
        for (Object param : params)
        {
            if (param instanceof Integer && ((Integer)param).intValue() == 0)
            {
                return true;
            }
        }
        return false;
    }
}
