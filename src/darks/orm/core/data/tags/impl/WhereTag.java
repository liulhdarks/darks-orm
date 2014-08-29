package darks.orm.core.data.tags.impl;

import org.dom4j.Element;

import darks.orm.core.data.tags.AbstractTag;

public class WhereTag extends TrimTag
{

	
	public WhereTag()
	{
		
	}
	
	public WhereTag(AbstractTag prevTag)
	{
		super(prevTag);
	}
	
    @Override
    public boolean parseElement(Element el)
    {
        prefix = "where";
        suffixOverrides = "and|or";
        prefixOverrides = "and|or";
        return true;
    }
	
}
