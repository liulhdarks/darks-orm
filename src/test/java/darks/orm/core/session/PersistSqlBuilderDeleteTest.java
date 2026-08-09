package darks.orm.core.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import darks.orm.core.session.model.LongIdEntity;

public class PersistSqlBuilderDeleteTest
{

    @Test
    public void buildDeleteSqlUsesPlaceholderForAnyPrimaryKeyType()
        throws Exception
    {
        String sql = PersistSqlBuilder.buildDeleteSql(LongIdEntity.class);
        assertNotNull(sql);
        assertTrue(sql.toLowerCase().startsWith("delete from"));
        assertTrue(sql.contains("?"));
        assertEquals("delete from long_id_entity where id = ?", sql.toLowerCase());
    }
}
