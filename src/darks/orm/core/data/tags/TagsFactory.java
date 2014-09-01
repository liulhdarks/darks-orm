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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Element;

import darks.orm.core.data.tags.impl.ElseIfTag;
import darks.orm.core.data.tags.impl.ElseTag;
import darks.orm.core.data.tags.impl.ForTag;
import darks.orm.core.data.tags.impl.IfTag;
import darks.orm.core.data.tags.impl.SetTag;
import darks.orm.core.data.tags.impl.TrimTag;
import darks.orm.core.data.tags.impl.WhereTag;

public final class TagsFactory
{

	static Map<String, Constructor<? extends AbstractTag>> tagsMap = new HashMap<String, Constructor<? extends AbstractTag>>();
	
	static
	{
		addTagClass("if", IfTag.class);
		addTagClass("elseif", ElseIfTag.class);
		addTagClass("else", ElseTag.class);
		addTagClass("foreach", ForTag.class);
		addTagClass("where", WhereTag.class);
		addTagClass("trim", TrimTag.class);
		addTagClass("set", SetTag.class);
	}
	
	private static void addTagClass(String key, Class<? extends AbstractTag> tagClass)
	{
		try
		{
			Constructor<? extends AbstractTag> crts = tagClass.getConstructor();
			tagsMap.put(key, crts);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static AbstractTag createTag(Element el, AbstractTag prevTag)
	{
		AbstractTag tag = createTag(el);
		if (tag != null)
		{
			tag.setPrevTag(prevTag);
		}
		return tag;
	}
	
	public static AbstractTag createTag(Element el)
	{
		String name = el.getName();
		Constructor<? extends AbstractTag> crts = tagsMap.get(name);
		if (crts == null)
		{
			return null;
		}
		
		try
		{
			return crts.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
