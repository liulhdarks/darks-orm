package darks.orm.core.data.tags.impl;

import java.util.List;

import org.dom4j.Element;

import darks.orm.core.data.tags.AbstractTag;
import darks.orm.core.data.xml.InterfaceMethodData;

public class TextTag extends AbstractTag
{
	
	protected String text;
	
	public TextTag()
	{
	}
	
	public TextTag(String text)
	{
		this.text = text;
	}
	
	public TextTag(String text, AbstractTag prevTag)
	{
		super(prevTag);
		this.text = text;
	}

	@Override
	public Object computeSql(StringBuilder sqlBuf, List<Object> params, InterfaceMethodData data, Object prevValue) throws Exception
	{
		sqlBuf.append(text.replaceAll("\n|\t", " ").trim()).append(' ');
		return null;
	}

	@Override
	public boolean parseElement(Element el)
	{
		text = el.getTextTrim();
		if ("".equals(text))
		{
			return false;
		}
		return true;
	}
	

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	@Override
	public String toString()
	{
		return "TextTag [text=" + text + "]";
	}

}
