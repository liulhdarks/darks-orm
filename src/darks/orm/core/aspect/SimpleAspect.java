package darks.orm.core.aspect;

import darks.orm.app.SqlSession;
import darks.orm.app.QueryEnumType;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.SimpleAspectWrapper;
import darks.orm.exceptions.AspectException;

public interface SimpleAspect
{
    
    public boolean beforeInvoke(SqlSession session, AspectData aspectData, SimpleAspectWrapper simpleWrapper,
        QueryEnumType queryEnumType)
        throws AspectException;
    
    public boolean afterInvoke(SqlSession session, AspectData aspectData, SimpleAspectWrapper simpleWrapper,
        QueryEnumType queryEnumType)
        throws AspectException;
}
