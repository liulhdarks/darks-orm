package darks.orm.core.data.tags.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import darks.orm.core.data.xml.InterfaceMethodData;

public class ForTagTest
{

    @Test
    public void computeSqlTrimsFullMultiCharSeparator()
        throws Exception
    {
        ForTag tag = newForTag("(", ")", ", ", "ids", "id");
        tag.addChild(new TextTag("#id"));

        InterfaceMethodData data = new InterfaceMethodData();
        data.addArgument(0, "ids");
        List<Object> params = new ArrayList<Object>();
        params.add(Arrays.asList(Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)));

        StringBuilder sql = new StringBuilder("select * from t where id in ");
        tag.computeSql(sql, params, data, null);

        String rendered = sql.toString().replace(" ", "");
        assertTrue("foreach should keep open/close: " + rendered, rendered.contains("in("));
        assertTrue("foreach should close the IN list: " + rendered, rendered.contains(")"));
        assertFalse("multi-char separator must not leave a trailing comma: " + rendered,
            rendered.contains(",)"));
        // collection occupies params[0], so generated aliases begin at #id1
        assertEquals("select*fromtwhereidin(#id1,#id2,#id3)", rendered);
    }

    @Test
    public void computeSqlAllowsNullCollection()
        throws Exception
    {
        ForTag tag = newForTag("(", ")", ",", "ids", "id");
        tag.addChild(new TextTag("#id"));

        InterfaceMethodData data = new InterfaceMethodData();
        data.addArgument(0, "ids");
        List<Object> params = new ArrayList<Object>();
        params.add(null);

        StringBuilder sql = new StringBuilder();
        tag.computeSql(sql, params, data, null);

        assertEquals("() ", sql.toString());
    }

    private static ForTag newForTag(String open, String close, String separator, String collection, String item)
    {
        ForTag tag = new ForTag();
        tag.open = open;
        tag.close = close;
        tag.separator = separator;
        tag.collection = collection;
        tag.item = item;
        return tag;
    }
}
