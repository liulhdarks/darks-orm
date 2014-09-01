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

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import darks.orm.core.data.tags.express.impl.AndOper;
import darks.orm.core.data.tags.express.impl.EqualOper;
import darks.orm.core.data.tags.express.impl.GtEqualOper;
import darks.orm.core.data.tags.express.impl.GtOper;
import darks.orm.core.data.tags.express.impl.ItEqualOper;
import darks.orm.core.data.tags.express.impl.ItOper;
import darks.orm.core.data.tags.express.impl.NotEqualOper;
import darks.orm.core.data.tags.express.impl.OrOper;
import darks.orm.core.data.xml.InterfaceMethodData;

/**
 * 采用逆波兰式进行表达式计算
 * @author lihua.llh
 *
 */
public class RPNCompute
{

	static Map<String, RPNOper> operMap = new HashMap<String, RPNOper>();
	
	static
	{
		addOperLevel(new NotEqualOper(), "!=");
		addOperLevel(new ItEqualOper(), "<=");
		addOperLevel(new GtEqualOper(), ">=");
		addOperLevel(new EqualOper(), "==");
		addOperLevel(new ItOper(), "<");
		addOperLevel(new GtOper(), ">");
		addOperLevel(new AndOper(), "&", "&&");
		addOperLevel(new OrOper(), "|", "||");
	}
	
	private static void addOperLevel(RPNOper op, String... opers)
	{
		for (String oper : opers)
		{
			operMap.put(oper, op);
		}
	}
	
	/**
	 * 计算表达式结果
	 * @param valQueue 操作数队列
	 * @param param 参数
	 * @return 计算结果
	 * @throws ExpressException
	 */
	public Object compute(LinkedList<String> valQueue, Object param) throws ExpressException
	{
	    if (valQueue.size() == 1)
        {
            return processSingle(valQueue, param);
        }
        if (valQueue.size() < 3)
        {
            throw new ExpressException("Invalid express queue which loss operation." + valQueue);
        }
	    if (valQueue == null || valQueue.isEmpty())
	    {
	        throw new ExpressException("Invalid express values queue.");
	    }
		@SuppressWarnings("unchecked")
		LinkedList<String> queue = (LinkedList<String>)valQueue.clone();
		LinkedList<Object> stack = new LinkedList<Object>();
		String op = null;
		while ((op = queue.poll()) != null)
		{
			if (operMap.containsKey(op))
			{
				computeOper(stack, op, param);
			}
			else
			{
				stack.push(op);
			}
		}
		if (stack.size() == 1)
		{
			return stack.pop();
		}
		else
		{
			throw new ExpressException("Invalid express which loss operation.Remain " + stack);
		}
	}
    
    private Object processSingle(LinkedList<String> valQueue, Object param)
    {
        String val = valQueue.get(0);
        String content = (String) param;
        return content.indexOf(val) >= 0;
    }
	
	private void computeOper(LinkedList<Object> stack, String op, Object param) throws ExpressException
	{
		RPNOper oper = operMap.get(op);
		if (oper == null)
		{
			throw new ExpressException("Invalid operation '\"" + op + "\' when executing express.");
		}
		oper.compute(stack, param);
	}
	
	
	public static void main(String[] args) throws ExpressException
	{
		RPNParser parser = new RPNParser();
		LinkedList<String> rule = parser.parse("userName == null && (nick=='sdfsdf' || p < 4)");
		System.out.println(rule);
		RPNCompute compute = new RPNCompute();
		InterfaceMethodData idata = new InterfaceMethodData();
		idata.addArgument(0, "userName");
		idata.addArgument(1, "nick");
		idata.addArgument(2, "p");
		Object[] params = new Object[]{"as", "sdfsd2f", 3};
		ExpressParam eparam = new ExpressParam(Arrays.asList(params), idata);
		System.out.println(compute.compute(rule, eparam));
	}
}
