package darks.orm.core.data.tags.impl;

import java.util.List;

import org.dom4j.Element;

import darks.orm.core.data.tags.AbstractTag;
import darks.orm.core.data.xml.InterfaceMethodData;

public class ElseTag extends AbstractTag
{
	
	public ElseTag()
	{
	}
	
	public ElseTag(AbstractTag prevTag)
	{
		super(prevTag);
	}

	@Override
	public boolean parseElement(Element el) throws Exception
	{
		return true;
	}
	
	@Override
	public Object computeSql(StringBuilder sqlBuf, List<Object> params, InterfaceMethodData data, Object prevValue) throws Exception
	{
		if (prevValue != null && (prevValue instanceof Boolean))
		{
			if ((Boolean)prevValue)
			{
				return null;
			}
		}
		super.computeSql(sqlBuf, params, data, prevValue);
		return null;
	}
	
	
}
