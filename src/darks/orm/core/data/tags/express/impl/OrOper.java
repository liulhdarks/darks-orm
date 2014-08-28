package darks.orm.core.data.tags.express.impl;

/**
 * Âß¼­»òÔËËã
 * @author lihua.llh
 *
 */
public class OrOper extends LogicOper
{

	public OrOper()
	{
		super("OR");
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public Object compute(Boolean src, Boolean dest)
    {
        return src || dest;
    }

}
