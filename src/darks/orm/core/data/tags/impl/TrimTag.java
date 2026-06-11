/**
 * 
 * Copyright 2014 The Darks ORM Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
		String strTmp = tmpBuf.toString().trim();
		if (strTmp.length() > 0)
		{
			if (prefixOverrides != null && !"".equals(prefixOverrides))
			{
	            String[] overrides = prefixOverrides.split("\\|");
	            for (String override : overrides)
	            {
	            	if ("".equals(override))
	            		continue;
	                override = override.trim();
	                if (matchesPrefixOverride(strTmp, override))
	                {
	                	strTmp = strTmp.substring(override.length()).trim();
	                }
	            }
			}
			if (suffixOverrides != null && !"".equals(suffixOverrides))
			{
				String[] overrides = suffixOverrides.split("\\|");
                for (String override : overrides)
                {
	            	if ("".equals(override))
	            		continue;
                    override = override.trim();
                    if (matchesSuffixOverride(strTmp, override))
                    {
                    	strTmp = strTmp.substring(0, strTmp.length() - override.length()).trim();
                    }
                }
			}
			if (strTmp.length() == 0)
			{
				return null;
			}
			if (prefix != null && !"".equals(prefix))
			{
				sqlBuf.append(' ').append(prefix).append(' ');
			}
			sqlBuf.append(strTmp).append(' ');
			if (suffix != null && !"".equals(suffix))
			{
				sqlBuf.append(' ').append(suffix).append(' ');
			}
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

	private boolean matchesPrefixOverride(String sql, String override)
	{
		if (!startsWithIgnoreCase(sql, override))
		{
			return false;
		}
		if (isIdentifierOverride(override) && sql.length() > override.length())
		{
			return !isSqlIdentifierChar(sql.charAt(override.length()));
		}
		return true;
	}

	private boolean matchesSuffixOverride(String sql, String override)
	{
		if (!endsWithIgnoreCase(sql, override))
		{
			return false;
		}
		int start = sql.length() - override.length();
		if (isIdentifierOverride(override) && start > 0)
		{
			return !isSqlIdentifierChar(sql.charAt(start - 1));
		}
		return true;
	}

	private boolean startsWithIgnoreCase(String sql, String override)
	{
		return sql.length() >= override.length() && sql.regionMatches(true, 0, override, 0, override.length());
	}

	private boolean endsWithIgnoreCase(String sql, String override)
	{
		int start = sql.length() - override.length();
		return start >= 0 && sql.regionMatches(true, start, override, 0, override.length());
	}

	private boolean isIdentifierOverride(String override)
	{
		for (int i = 0; i < override.length(); i++)
		{
			if (!isSqlIdentifierChar(override.charAt(i)))
			{
				return false;
			}
		}
		return override.length() > 0;
	}

	private boolean isSqlIdentifierChar(char ch)
	{
		return Character.isLetterOrDigit(ch) || ch == '_';
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
