package darks.orm.core.data.tags.impl;

import java.util.List;

import org.dom4j.Element;

import darks.orm.core.data.tags.AbstractTag;
import darks.orm.core.data.xml.InterfaceMethodData;

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
