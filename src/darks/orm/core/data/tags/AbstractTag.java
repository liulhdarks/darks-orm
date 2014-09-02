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

package darks.orm.core.data.tags;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import darks.orm.core.data.xml.InterfaceMethodData;

public abstract class AbstractTag
{
	
	protected String name;
	
	protected List<AbstractTag> childrenTags = new ArrayList<AbstractTag>();
	
	protected AbstractTag prevTag;
	
	public AbstractTag()
	{
		
	}
	
	public AbstractTag(AbstractTag prevTag)
	{
		this.prevTag = prevTag;
	}
	
	public void addChild(AbstractTag tag)
	{
		childrenTags.add(tag);
	}
	
	public Object[] computeSql(StringBuilder sqlBuf, Object[] params, InterfaceMethodData data) throws Exception
	{
		List<Object> paramList = new ArrayList<Object>(params.length);
		for (Object param : params)
		{
			paramList.add(param);
		}
		computeSql(sqlBuf, paramList, data, null);
		return paramList.toArray();
	}
	
	public Object computeSql(StringBuilder sqlBuf, List<Object> params, InterfaceMethodData data, Object prevValue) throws Exception
	{
		for (AbstractTag tag : childrenTags)
		{
			prevValue = tag.computeSql(sqlBuf, params, data, prevValue);
		}
		return null;
	}
	
	public abstract boolean parseElement(Element el) throws Exception;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public AbstractTag getPrevTag()
	{
		return prevTag;
	}

	public void setPrevTag(AbstractTag prevTag)
	{
		this.prevTag = prevTag;
	}
	
	
}
