package darks.orm.core.aspect;

import darks.orm.app.SqlSession;
import darks.orm.app.QueryEnumType;
import darks.orm.core.aspect.jclass.JavaClassQueryAspect;
import darks.orm.core.aspect.jython.PythonQueryAspect;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.core.data.xml.AspectData.AspectType;
import darks.orm.exceptions.AspectException;

public class AspectQueryContext implements QueryAspect
{
    
    private QueryAspectAdapter queryAspect;
    
    private AspectData aspectData;
    
    public AspectQueryContext(AspectData aspectData)
    {
        this.aspectData = aspectData;
        queryAspect = createQueryAspect(aspectData);
    }
    
    /**
     * ¸ù¾ÝA
     * 
     * @param espectData
     * @return
     */
    public QueryAspectAdapter createQueryAspect(AspectData aspectData)
    {
        if (aspectData == null)
            return null;
        AspectType aspectType = aspectData.getAspectType();
        if (aspectType == AspectType.PYFILE || aspectType == AspectType.JYTHON)
        {
            return new PythonQueryAspect();
        }
        else if (aspectType == AspectType.JAVACLASS)
        {
            return new JavaClassQueryAspect();
        }
        else
        {
            return null;
        }
    }
    
    public boolean beforeInvoke(SqlSession dao, AspectData espectData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (queryAspect == null)
            return false;
        return queryAspect.beforeInvoke(dao, espectData, queryWrapper, queryEnumType);
    }
    
    public boolean afterInvoke(SqlSession dao, AspectData espectData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (queryAspect == null)
            return false;
        return queryAspect.afterInvoke(dao, espectData, queryWrapper, queryEnumType);
    }
    
    public QueryAspectAdapter getQueryAspect()
    {
        return queryAspect;
    }
    
    public void setQueryAspect(QueryAspectAdapter queryAspect)
    {
        this.queryAspect = queryAspect;
    }
    
    public AspectData getAspectData()
    {
        return aspectData;
    }
    
    public void setAspectData(AspectData aspectData)
    {
        this.aspectData = aspectData;
    }
    
}
