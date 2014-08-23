
package darks.orm.core.executor;

import java.sql.SQLException;

import darks.orm.app.SqlSession;
import darks.orm.core.aspect.AspectUpdateContext;
import darks.orm.core.data.xml.DMLUpdateData;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.core.data.xml.SimpleAspectWrapper;

public class SqlMapUpdateExecutor extends SqlMapExecutorAdapter
{
    
    private DMLUpdateData updateData;
    
    private SimpleAspectWrapper simpleWrapper;
    
    private AspectUpdateContext aspectUpdateContext;
    
    private SqlSession session;
    
    public SqlMapUpdateExecutor()
    {
        
    }
    
    public SqlMapUpdateExecutor(SqlSession session, DMLUpdateData updateData, SimpleAspectWrapper simpleWrapper)
    {
        this.updateData = updateData;
        this.simpleWrapper = simpleWrapper;
        this.session = session;
    }
    
    @Override
    public boolean initialize()
    {
        aspectUpdateContext = new AspectUpdateContext(updateData.getAspectData());
        return true;
    }
    
    @Override
    public Object bodyInvoke()
        throws SQLException
    {
        session.executeUpdate(simpleWrapper.getSql(), simpleWrapper.getParams());
        return null;
    }
    
    @Override
    public boolean afterInvoke()
    {
        return aspectUpdateContext.afterInvoke(session, updateData.getAspectData(), simpleWrapper, null);
    }
    
    @Override
    public boolean beforeInvoke()
    {
        return aspectUpdateContext.beforeInvoke(session, updateData.getAspectData(), simpleWrapper, null);
    }
    
    public DMLUpdateData getUpdateData()
    {
        return updateData;
    }
    
    public void setUpdateData(DMLUpdateData updateData)
    {
        this.updateData = updateData;
    }
    
    public SimpleAspectWrapper getSqlMapAspectData()
    {
        return simpleWrapper;
    }
    
    public void setSqlMapAspectData(QueryAspectWrapper simpleWrapper)
    {
        this.simpleWrapper = simpleWrapper;
    }
    
    public AspectUpdateContext getAspectUpdateContext()
    {
        return aspectUpdateContext;
    }
    
    public void setAspectUpdateContext(AspectUpdateContext aspectUpdateContext)
    {
        this.aspectUpdateContext = aspectUpdateContext;
    }
    
}
