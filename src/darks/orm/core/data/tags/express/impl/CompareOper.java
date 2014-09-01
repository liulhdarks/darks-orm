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

import java.util.LinkedList;

import darks.orm.core.data.tags.express.ExpressException;
import darks.orm.core.data.tags.express.ExpressParam;
import darks.orm.core.data.tags.express.RPNOper;

/**
 * 逻辑运算符基类
 * @author lihua.llh
 *
 */
public abstract class CompareOper implements RPNOper
{
	
	private String name;

	public CompareOper(String name)
	{
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void compute(LinkedList<Object> stack, Object param) throws ExpressException
	{
		if (stack.size() < 2)
		{
			throw new ExpressException(name + " oper Need two values. Which only has " + stack);
		}
		Object val2 = (Object) stack.pop();
		Object val1 = (Object) stack.pop();
		Object result = null;
		ExpressParam expParam = (ExpressParam) param;
		if ((val1 instanceof String))
		{
			val1 = expParam.getParam((String) val1);
		}
		if ((val2 instanceof String))
		{
			val2 = expParam.getParam((String) val2);
		}
		if (val1 == null || val2 == null)
		{
			result = compute(val1, val2);
			stack.push(result);
		}
		else if ((val1 instanceof String) && (val2 instanceof String))
		{
			result = compute((String) val1, (String) val2);
			stack.push(result);
		}
		else if ((val1 instanceof Integer) && (val2 instanceof Integer))
		{
			result = compute((Integer) val1, (Integer) val2);
			stack.push(result);
		}
		else if ((val1 instanceof Long) && (val2 instanceof Long))
		{
			result = compute((Long) val1, (Long) val2);
			stack.push(result);
		}
		else
		{
			throw new ExpressException("Invalid data type when compute RPN express." 
					+ val1 + " " + name + " " + val2);
		}
	}

	/**
	 * 运算
	 * @param val1 源操作数
	 * @param val2 目标操作数
	 * @return 运算结果
	 */
	public abstract Boolean compute(String src, String desc);

	/**
	 * 运算
	 * @param val1 源操作数
	 * @param val2 目标操作数
	 * @return 运算结果
	 */
	public abstract Boolean compute(int src, int desc);

	/**
	 * 运算
	 * @param val1 源操作数
	 * @param val2 目标操作数
	 * @return 运算结果
	 */
	public abstract Boolean compute(long src, long desc);

	/**
	 * 运算
	 * @param val1 源操作数
	 * @param val2 目标操作数
	 * @return 运算结果
	 */
	public abstract Boolean compute(Object src, Object desc);
}
