package darks.orm.core.aspect.jython;

import darks.orm.app.SqlSession;
import darks.orm.app.QueryEnumType;
import darks.orm.core.aspect.QueryAspectAdapter;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.exceptions.AspectException;
import darks.orm.util.PluginHelper;

public class PythonQueryAspect extends QueryAspectAdapter
{
    
    private PythonParser pythonParser = null;
    
    public PythonQueryAspect()
    {
        
    }
    
    @Override
    public boolean beforeInvoke(SqlSession dao, AspectData aspectData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (aspectData == null)
            return true;
        if (PluginHelper.isRegJythonPlugin())
        {
            PythonParser parser = getPythonParser();
            return parser.parse(aspectData, queryWrapper, queryEnumType, PythonBuilder.JY_ESPECT_BEFORE);
        }
        return true;
    }
    
    @Override
    public boolean afterInvoke(SqlSession dao, AspectData aspectData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (aspectData == null)
            return true;
        if (PluginHelper.isRegJythonPlugin())
        {
            PythonParser parser = getPythonParser();
            return parser.parse(aspectData, queryWrapper, queryEnumType, PythonBuilder.JY_ESPECT_AFTER);
        }
        return true;
    }
    
    private PythonParser getPythonParser()
    {
        if (pythonParser == null)
        {
            if (PluginHelper.isRegJythonPlugin())
            {
                pythonParser = new PythonParser();
            }
            else
            {
                return null;
            }
        }
        return pythonParser;
    }
    
}
