package darks.orm.core.data.tags.impl;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

import darks.orm.core.data.tags.AbstractTag;
import darks.orm.core.data.tags.express.ExpressParam;
import darks.orm.core.data.tags.express.RPNCompute;
import darks.orm.core.data.tags.express.RPNParser;
import darks.orm.core.data.xml.InterfaceMethodData;

public class IfTag extends AbstractTag
{

	String condition;
	
	LinkedList<String> conditionList;
	
	static RPNCompute compute = new RPNCompute();
	
	public IfTag()
	{
	}
	
	public IfTag(AbstractTag prevTag)
	{
		super(prevTag);
	}

	@Override
	public boolean parseElement(Element el) throws Exception
	{
		condition = el.attributeValue("test").trim();
		boolean suc = !"".equals(condition);
		if (suc)
		{
			RPNParser parser = new RPNParser();
			conditionList = parser.parse(condition);
		}
		return suc && conditionList != null;
	}
	
	@Override
	public Object computeSql(StringBuilder sqlBuf, List<Object> params, InterfaceMethodData data, Object prevValue) throws Exception
	{
		ExpressParam expParam = new ExpressParam(params, data);
		boolean ret = (Boolean)compute.compute(conditionList, expParam);
		if (ret)
		{
			super.computeSql(sqlBuf, params, data, prevValue);
		}
		return ret;
	}

	public String getCondition()
	{
		return condition;
	}

	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	@Override
	public String toString()
	{
		return "IfTag [condition=" + condition + ", childrenTags=" + childrenTags + "]";
	}

	
	
}
