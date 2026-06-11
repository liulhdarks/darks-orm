package darks.orm.test;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.junit.Assert;
import org.junit.Test;

import darks.orm.core.config.sqlmap.DMLConfigReader;
import darks.orm.core.config.sqlmap.SqlMapConfiguration;
import darks.orm.core.data.xml.DMLData;

public class SqlMapDynamicTagTest
{

	@Test
	public void forwardIncludeIsRendered()
		throws Exception
	{
		SqlMapConfiguration config = readDml("<DML namespace=\"User\">" +
			"<Update id=\"deleteById\">delete from users <include refid=\"byId\"/></Update>" +
			"<tag id=\"byId\">where id = #1</tag>" +
			"</DML>");

		String sql = renderSql(config, "User.deleteById", new Object[] {Integer.valueOf(1)});

		Assert.assertTrue(sql.indexOf("delete from users") >= 0);
		Assert.assertTrue(sql.indexOf("where id = #1") >= 0);
	}

	@Test
	public void whereDoesNotStripIdentifierPrefixes()
		throws Exception
	{
		SqlMapConfiguration config = readDml("<DML namespace=\"Order\">" +
			"<Update id=\"deleteById\">delete from orders <where>order_id = #1</where></Update>" +
			"</DML>");

		String sql = renderSql(config, "Order.deleteById", new Object[] {Integer.valueOf(1)});

		Assert.assertTrue(sql.indexOf("where order_id = #1") >= 0);
		Assert.assertFalse(sql.indexOf("der_id") >= 0);
	}

	private SqlMapConfiguration readDml(String xml)
		throws Exception
	{
		SqlMapConfiguration config = new SqlMapConfiguration();
		DMLConfigReader reader = new DMLConfigReader(config);
		Document document = DocumentHelper.parseText(xml);
		reader.reader(document.getRootElement());
		return config;
	}

	private String renderSql(SqlMapConfiguration config, String id, Object[] params)
		throws Exception
	{
		DMLData data = config.getDMLData(id);
		Assert.assertNotNull(data);
		StringBuilder sql = new StringBuilder();
		data.getSqlTag().computeSql(sql, params, null);
		return sql.toString();
	}

}
