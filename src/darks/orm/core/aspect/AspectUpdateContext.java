package darks.orm.core.aspect;

import darks.orm.app.SqlSession;
import darks.orm.app.QueryEnumType;
import darks.orm.core.aspect.jclass.JavaClassUpdateAspect;
import darks.orm.core.aspect.jython.PythonUpdateAspect;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.SimpleAspectWrapper;
import darks.orm.core.data.xml.AspectData.AspectType;
import darks.orm.exceptions.AspectException;

public class AspectUpdateContext implements SimpleAspect
{
    
    private UpdateAspectAdapter updateAspect;
    
    private AspectData aspectData;
    
    public AspectUpdateContext(AspectData aspectData)
    {
        this.aspectData = aspectData;
        updateAspect = createUpdateAspect(aspectData);
    }
    
    /**
     * 根据aspectData创建处理实例
     * 
     * @param espectData
     * @return
     */
    public UpdateAspectAdapter createUpdateAspect(AspectData aspectData)
    {
        if (aspectData == null)
            return null;
        AspectType aspectType = aspectData.getAspectType();
        if (aspectType == AspectType.PYFILE || aspectType == AspectType.JYTHON)
        {
            return new PythonUpdateAspect();
        }
        else if (aspectType == AspectType.JAVACLASS)
        {
            return new JavaClassUpdateAspect();
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public boolean afterInvoke(SqlSession session, AspectData aspectData, SimpleAspectWrapper simpleWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (updateAspect == null)
            return false;
        return updateAspect.afterInvoke(session, aspectData, simpleWrapper, queryEnumType);
    }
    
    @Override
    public boolean beforeInvoke(SqlSession session, AspectData aspectData, SimpleAspectWrapper simpleWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (updateAspect == null)
            return false;
        return updateAspect.beforeInvoke(session, aspectData, simpleWrapper, queryEnumType);
    }
    
    public UpdateAspectAdapter getUpdateAspect()
    {
        return updateAspect;
    }
    
    public void setUpdateAspect(UpdateAspectAdapter updateAspect)
    {
        this.updateAspect = updateAspect;
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
