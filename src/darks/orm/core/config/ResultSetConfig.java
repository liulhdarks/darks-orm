
package darks.orm.core.config;

import java.sql.ResultSet;

import darks.orm.datasource.factory.StatementFactory.StatementType;

public class ResultSetConfig
{
    private StatementType stateType;
    
    private int type;
    
    private int concurrency;
    
    private boolean sensitive;
    
    public ResultSetConfig()
    {
        stateType = StatementType.Normal;
        type = ResultSet.TYPE_FORWARD_ONLY;
        sensitive = true;
        concurrency = ResultSet.CONCUR_READ_ONLY;
    }
    
    public void update()
    {
        if (stateType == StatementType.Normal)
        {
            type = ResultSet.TYPE_FORWARD_ONLY;
        }
        else
        {
            if (sensitive)
            {
                type = ResultSet.TYPE_SCROLL_SENSITIVE;
            }
            else
            {
                type = ResultSet.TYPE_SCROLL_INSENSITIVE;
            }
        }
    }
    
    public void setType(String rsType)
    {
        if ("forward".equalsIgnoreCase(rsType))
        {
            stateType = StatementType.Normal;
        }
        else if ("scroll".equalsIgnoreCase(rsType))
        {
            stateType = StatementType.Scorllable;
        }
        update();
    }
    
    public void setSensitive(String isSensitive)
    {
        if ("true".equalsIgnoreCase(isSensitive))
        {
            sensitive = true;
        }
        else if ("false".equalsIgnoreCase(isSensitive))
        {
            sensitive = false;
        }
    }
    
    public void setConcurrency(String concurr)
    {
        if ("read".equalsIgnoreCase(concurr))
        {
            concurrency = ResultSet.CONCUR_READ_ONLY;
        }
        else if ("updatable".equalsIgnoreCase(concurr))
        {
            concurrency = ResultSet.CONCUR_UPDATABLE;
        }
    }
    
    public StatementType getStatementType()
    {
        return stateType;
    }
    
    public int getType()
    {
        return type;
    }
    
    public int getConcurrency()
    {
        return concurrency;
    }
    
    public boolean isSensitive()
    {
        return sensitive;
    }
    
}
