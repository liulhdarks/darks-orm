package darks.orm.core.aspect;

import darks.orm.app.SqlSession;
import darks.orm.app.QueryEnumType;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.exceptions.AspectException;

public abstract class QueryAspectAdapter implements QueryAspect
{
    
    public QueryAspectAdapter()
    {
        
    }
    
    @Override
    public boolean afterInvoke(SqlSession session, AspectData aspectData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        return true;
    }
    
    @Override
    public boolean beforeInvoke(SqlSession session, AspectData aspectData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        return true;
    }
    
}
