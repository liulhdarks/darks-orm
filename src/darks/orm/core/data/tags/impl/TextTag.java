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
