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

package darks.orm.core.data.tags.express.impl;

public class NotEqualOper extends CompareOper
{

	public NotEqualOper()
	{
		super("NE");
	}

	@Override
	public Boolean compute(String src, String desc)
	{
		if (src == null)
		{
			return desc != null;
		}
		else
		{
			return !src.equals(desc);
		}
	}

	@Override
	public Boolean compute(int src, int desc)
	{
		return src != desc;
	}

	@Override
	public Boolean compute(long src, long desc)
	{
		return src != desc;
	}

	@Override
	public Boolean compute(Object src, Object desc)
	{
		if (src == null)
		{
			return desc != null;
		}
		else
		{
			return !src.equals(desc);
		}
	}

}
