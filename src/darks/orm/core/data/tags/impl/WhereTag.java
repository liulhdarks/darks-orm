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
