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

}
