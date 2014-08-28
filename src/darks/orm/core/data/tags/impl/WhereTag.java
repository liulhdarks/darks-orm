package darks.orm.core.data.tags.impl;

import java.util.List;

import org.dom4j.Element;

import darks.orm.core.data.tags.AbstractTag;
import darks.orm.core.data.xml.InterfaceMethodData;

public class WhereTag extends AbstractTag
{

	
	public WhereTag()
	{
		
	}
	
	public WhereTag(AbstractTag prevTag)
	{
		super(prevTag);
	}
	
	@Override
	public Object computeSql(StringBuilder sqlBuf, List<Object> params, InterfaceMethodData data, Object prevValue) throws Exception
	{
		StringBuilder tmpBuf = new StringBuilder();
		super.computeSql(tmpBuf, params, data, prevValue);
		if (tmpBuf.length() > 0)
		{
			sqlBuf.append(" where ").append(tmpBuf);
		}
		return null;
	}

	@Override
	public boolean parseElement(Element el)
	{
		return true;
	}
	
}
