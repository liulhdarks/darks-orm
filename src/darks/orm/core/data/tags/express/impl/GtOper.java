package darks.orm.core.data.tags.express.impl;

public class GtOper extends CompareOper
{

	public GtOper()
	{
		super("GT");
	}

	@Override
	public Boolean compute(String src, String desc)
	{
		if (src == null)
		{
			return desc == null;
		}
		else
		{
			return src.compareTo(desc) > 0;
		}
	}

	@Override
	public Boolean compute(int src, int desc)
	{
		return src > desc;
	}

	@Override
	public Boolean compute(long src, long desc)
	{
		return src > desc;
	}

}
