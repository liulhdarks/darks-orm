package darks.orm.core.data.tags.impl;

import java.util.List;

import org.dom4j.Element;

import darks.orm.core.data.tags.AbstractTag;
import darks.orm.core.data.xml.InterfaceMethodData;

public class TrimTag extends AbstractTag
{

	protected String prefix;
	
	protected String prefixOverrides;

	protected String suffix;
	
	protected String suffixOverrides;
	
	
	public TrimTag()
	{
		
	}
	
	public TrimTag(AbstractTag prevTag)
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
			if (prefixOverrides != null && !"".equals(prefixOverrides))
			{
	            String[] overrides = prefixOverrides.split("|");
	            for (String override : overrides)
	            {
	                override = override.trim();
	                if (tmpBuf.indexOf(override) == 0)
	                {
	                    tmpBuf.substring(override.length());
	                }
	            }
			}
			if (suffixOverrides != null && !"".equals(suffixOverrides))
			{
				String[] overrides = suffixOverrides.split("|");
                for (String override : overrides)
                {
                    override = override.trim();
                    int index = tmpBuf.lastIndexOf(override);
                    if (index == tmpBuf.length() - override.length())
                    {
                        tmpBuf.substring(override.length());
                    }
                }
			}
			if (suffix != null && !"".equals(suffix))
			{
				tmpBuf.append(' ').append(suffix).append(' ');
			}
			if (prefix != null && !"".equals(prefix))
			{
				sqlBuf.append(' ').append(prefix).append(' ');
			}
			sqlBuf.append(tmpBuf);
		}
		return null;
	}

	@Override
	public boolean parseElement(Element el)
	{
		prefix = el.attributeValue("prefix").trim();
		prefixOverrides = el.attributeValue("prefixOverrides").trim();
		suffix = el.attributeValue("suffix").trim();
		suffixOverrides = el.attributeValue("suffixOverrides").trim();
		return true;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public String getPrefixOverrides()
	{
		return prefixOverrides;
	}

	public void setPrefixOverrides(String prefixOverrides)
	{
		this.prefixOverrides = prefixOverrides;
	}

	public String getSuffix()
	{
		return suffix;
	}

	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}

	public String getSuffixOverrides()
	{
		return suffixOverrides;
	}

	public void setSuffixOverrides(String suffixOverrides)
	{
		this.suffixOverrides = suffixOverrides;
	}
	
}
