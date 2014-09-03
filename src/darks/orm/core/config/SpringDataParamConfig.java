package darks.orm.core.config;

public class SpringDataParamConfig
{
	private String resultSetType;
    
    private String concurrency;
    
    private String sensitive;
    
    private boolean autoCommit;
    
    public SpringDataParamConfig()
    {
    	resultSetType = "scroll";
    	concurrency = "read";
    	sensitive = "false";
    	autoCommit = true;
    }

	public String getResultSetType()
	{
		return resultSetType;
	}

	public void setResultSetType(String resultSetType)
	{
		this.resultSetType = resultSetType;
	}

	public String getConcurrency()
	{
		return concurrency;
	}

	public void setConcurrency(String concurrency)
	{
		this.concurrency = concurrency;
	}

	public String getSensitive()
	{
		return sensitive;
	}

	public void setSensitive(String sensitive)
	{
		this.sensitive = sensitive;
	}

	public boolean isAutoCommit()
	{
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit)
	{
		this.autoCommit = autoCommit;
	}

	@Override
	public String toString()
	{
		return "SpringDataParamConfig [resultSetType=" + resultSetType + ", concurrency="
				+ concurrency + ", sensitive=" + sensitive + ", autoCommit=" + autoCommit + "]";
	}
	
    
}
