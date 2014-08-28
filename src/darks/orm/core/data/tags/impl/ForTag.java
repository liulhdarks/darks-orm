package darks.orm.core.data.tags.impl;

import java.util.Collection;
import java.util.List;

import org.dom4j.Element;

import darks.orm.core.data.tags.AbstractTag;
import darks.orm.core.data.xml.InterfaceMethodData;
import darks.orm.exceptions.ConfigException;

public class ForTag extends AbstractTag
{

	String open;
	
	String close;
	
	String separator;
	
	String collection;
	
	String item;
	
	public ForTag()
	{
		
	}
	
	public ForTag(AbstractTag prevTag)
	{
		super(prevTag);
	}
	
	@Override
	public Object computeSql(StringBuilder sqlBuf, List<Object> params, InterfaceMethodData data, Object prevValue) throws Exception
	{
		Integer index = data.getArgumentIndex(collection);
		if (index == null)
		{
			return null;
		}
		@SuppressWarnings("unchecked")
		Collection<Object> col = (Collection<Object>)params.get(index);
		int i = params.size();
		sqlBuf.append(open);
		for (Object obj : col)
		{
			params.add(obj);
			StringBuilder buf = new StringBuilder();
			super.computeSql(buf, params, data, prevValue);
			String p = buf.toString();
			String itemKey = item + i;
			String key = "#" + itemKey;
			key = p.replace("#" + item, key).trim();
			data.addArgument(i, itemKey);
			sqlBuf.append(key).append(separator);
			i++;
		}
		if (!col.isEmpty())
		{
			sqlBuf.setLength(sqlBuf.length() - 1);
		}
		sqlBuf.append(close).append(' ');
		return null;
	}

	@Override
	public boolean parseElement(Element el)
	{
		open = el.attributeValue("open").trim();
		close = el.attributeValue("close").trim();
		separator = el.attributeValue("separator").trim();
		collection = el.attributeValue("collection").trim();
		item = el.attributeValue("item").trim();
		if ("".equals(open))
		{
			throw new ConfigException("Sqlmap foreach tag's attribute 'open' is invalid.");
		}
		if ("".equals(close))
		{
			throw new ConfigException("Sqlmap foreach tag's attribute 'close' is invalid.");
		}
		if ("".equals(separator))
		{
			throw new ConfigException("Sqlmap foreach tag's attribute 'separator' is invalid.");
		}
		if ("".equals(collection))
		{
			throw new ConfigException("Sqlmap foreach tag's attribute 'collection' is invalid.");
		}
		if ("".equals(item))
		{
			throw new ConfigException("Sqlmap foreach tag's attribute 'item' is invalid.");
		}
		return true;
	}
	
}
