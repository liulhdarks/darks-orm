package darks.orm.core.data.tags.express.impl;

import java.util.LinkedList;

import darks.orm.core.data.tags.express.ExpressException;
import darks.orm.core.data.tags.express.RPNOper;

/**
 * 逻辑运算符基类
 * @author lihua.llh
 *
 */
public abstract class LogicOper implements RPNOper
{
	
	private String name;

	public LogicOper(String name)
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
		if ((val1 instanceof Boolean) && (val2 instanceof Boolean))
		{
			result = compute((Boolean) val1, (Boolean) val2);
			stack.push(result);
		}
		else
		{
			throw new ExpressException("Invalid data type when compute RPN express." 
					+ val1 + " " + name + " " + val2);
		}
	}

	/**
	 * 对布尔变量进行逻辑运算
	 * @param val1 源操作数
	 * @param val2 目标操作数
	 * @return 运算结果
	 */
	public abstract Object compute(Boolean src, Boolean desc);
}
