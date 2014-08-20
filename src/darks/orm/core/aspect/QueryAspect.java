package darks.orm.core.aspect;

import darks.orm.app.SqlSession;
import darks.orm.app.QueryEnumType;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.exceptions.AspectException;

public interface QueryAspect
{
    
    public boolean beforeInvoke(SqlSession session, AspectData aspectData, QueryAspectWrapper sqlMapAspectData,
        QueryEnumType queryEnumType)
        throws AspectException;
    
    public boolean afterInvoke(SqlSession session, AspectData aspectData, QueryAspectWrapper sqlMapAspectData,
        QueryEnumType queryEnumType)
        throws AspectException;
}
