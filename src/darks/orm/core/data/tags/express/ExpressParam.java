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

package darks.orm.core.data.tags.express;

import java.util.List;

import darks.orm.core.data.xml.InterfaceMethodData;
import darks.orm.util.SqlHelper;

public class ExpressParam
{
	
	public static final String STR_FLAG = "S@";
	
	public static final String INT_FLAG = "I@";
	
	public static final String LONG_FLAG = "L@";
	
	private static final String INDEX_FLAG = "#";

	private List<Object> params;
	
	private InterfaceMethodData data;

	public ExpressParam(List<Object> params, InterfaceMethodData data)
	{
		super();
		this.params = params;
		this.data = data;
	}
	
	public Object getParam(String paramName) throws ExpressException
	{
		if (paramName == null || "null".equalsIgnoreCase(paramName))
		{
			return null;
		}
		Object result = getConstParam(paramName);
		if (result != null)
		{
			return result;
		}
		return SqlHelper.getParamSubObject(params, data.getArgumentsMap(), paramName);
//		int index = -1;
//		if (paramName.startsWith(INDEX_FLAG))
//		{
//			paramName = paramName.substring(INDEX_FLAG.length());
//			try
//			{
//				index = Integer.parseInt(paramName) - 1;
//			}
//			catch (Exception e)
//			{
//			}
//		}
//		if (index < 0)
//		{
//			index = data.getArgumentIndex(paramName);
//		}
//		if (index < 0)
//		{
//			throw new ExpressException("Sqlmap express param " + paramName + " is invalid");
//		}
//		if (index >= params.size())
//		{
//			throw new ExpressException("Sqlmap express param " + paramName + "'s index out of params array index.");
//		}
//		return params.get(index);
	}
	
	public Object getConstParam(String paramName)
	{
		if (paramName.startsWith(STR_FLAG))
		{
			return paramName.substring(STR_FLAG.length());
		}
		else if (paramName.startsWith(INT_FLAG))
		{
			String strInt = paramName.substring(INT_FLAG.length());
			return Integer.parseInt(strInt);
		}
		else if (paramName.startsWith(LONG_FLAG))
		{
			String strLong = paramName.substring(LONG_FLAG.length());
			return Long.parseLong(strLong);
		}
		return null;
	}
	
}
