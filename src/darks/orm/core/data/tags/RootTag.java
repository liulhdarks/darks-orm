package darks.orm.core.data.tags;

import org.dom4j.Element;

public class RootTag extends AbstractTag
{

	@Override
	public boolean parseElement(Element el)
	{
		return true;
	}

}
