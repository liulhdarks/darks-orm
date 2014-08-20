package darks.orm.core.aspect.jython;

import darks.orm.app.QueryEnumType;
import darks.orm.app.SqlSession;
import darks.orm.core.aspect.UpdateAspectAdapter;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.SimpleAspectWrapper;
import darks.orm.exceptions.AspectException;
import darks.orm.util.PluginHelper;

public class PythonUpdateAspect extends UpdateAspectAdapter
{
    
    private PythonParser pythonParser = null;
    
    public PythonUpdateAspect()
    {
        
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
    
    @Override
    public boolean afterInvoke(SqlSession session, AspectData aspectData, SimpleAspectWrapper simpleWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (aspectData == null)
            return true;
        if (PluginHelper.isRegJythonPlugin())
        {
            PythonParser parser = getPythonParser();
            return parser.parse(aspectData, simpleWrapper, null, PythonBuilder.JY_ESPECT_AFTER);
        }
        return true;
    }
    
    @Override
    public boolean beforeInvoke(SqlSession session, AspectData aspectData, SimpleAspectWrapper simpleWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (aspectData == null)
            return true;
        if (PluginHelper.isRegJythonPlugin())
        {
            PythonParser parser = getPythonParser();
            return parser.parse(aspectData, simpleWrapper, null, PythonBuilder.JY_ESPECT_BEFORE);
        }
        return true;
    }
    
}
