package darks.orm.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import darks.orm.core.data.SqlParamData;
import darks.orm.core.session.model.FkOwner;
import darks.orm.core.session.model.FkRef;

public class SqlHelperBuildSqlParamsTest
{

    @Test
    public void buildSqlParamsAllowsNullNestedAssociation()
        throws Exception
    {
        FkOwner owner = new FkOwner();
        owner.setId(Integer.valueOf(5));
        owner.setRef(null);

        Map<String, Integer> argMap = new HashMap<String, Integer>();
        argMap.put("owner", Integer.valueOf(0));

        List<SqlParamData> dataList = SqlHelper.parseSqlParams("update t set ref_id = #owner.ref.id where id = #owner.id");
        List<Object> params = SqlHelper.buildSqlParams(dataList, argMap, new Object[] {owner});

        assertEquals(2, params.size());
        assertNull("null nested path should bind SQL NULL", params.get(0));
        assertEquals(Integer.valueOf(5), params.get(1));
    }

    @Test
    public void buildSqlParamsReadsNestedAssociationField()
        throws Exception
    {
        FkRef ref = new FkRef();
        ref.setId(Integer.valueOf(9));
        FkOwner owner = new FkOwner();
        owner.setId(Integer.valueOf(5));
        owner.setRef(ref);

        Map<String, Integer> argMap = new HashMap<String, Integer>();
        argMap.put("owner", Integer.valueOf(0));

        List<SqlParamData> dataList = SqlHelper.parseSqlParams("update t set ref_id = #owner.ref.id where id = #owner.id");
        List<Object> params = SqlHelper.buildSqlParams(dataList, argMap, new Object[] {owner});

        assertEquals(Integer.valueOf(9), params.get(0));
        assertEquals(Integer.valueOf(5), params.get(1));
    }
}
