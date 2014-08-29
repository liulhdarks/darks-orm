package darks.orm.core.data.tags.impl;

import org.dom4j.Element;

import darks.orm.core.data.tags.AbstractTag;

public class SetTag extends TrimTag
{

	
	public SetTag()
	{
		
	}
	
	public SetTag(AbstractTag prevTag)
	{
		super(prevTag);
	}

	@Override
	public boolean parseElement(Element el)
	{
		prefix = "set";
		suffixOverrides = ",";
		return true;
	}
	
}
