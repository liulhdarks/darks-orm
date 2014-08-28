package darks.orm.core.data.tags.express.impl;

public class GtEqualOper extends CompareOper
{

	public GtEqualOper()
	{
		super("GTE");
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
			return src.compareTo(desc) >= 0;
		}
	}

	@Override
	public Boolean compute(int src, int desc)
	{
		return src >= desc;
	}

	@Override
	public Boolean compute(long src, long desc)
	{
		return src >= desc;
	}

}
