package darks.orm.core.aspect.js;

import darks.orm.app.QueryEnumType;
import darks.orm.app.SqlSession;
import darks.orm.core.aspect.UpdateAspectAdapter;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.SimpleAspectWrapper;

public class JsUpdateAspect extends UpdateAspectAdapter
{
    
    private JsParser jsParser = null;
    
    public JsUpdateAspect()
    {
        
    }
    
    private JsParser getJsParser()
    {
        if (jsParser == null)
        {
            jsParser = new JsParser();
        }
        return jsParser;
    }
    
    @Override
    public boolean afterInvoke(SqlSession dao, AspectData aspectData, SimpleAspectWrapper simpleWrapper,
        QueryEnumType queryEnumType)
    {
        if (aspectData == null)
            return true;
        JsParser parser = getJsParser();
        return parser.parse(aspectData, simpleWrapper, null, "after");
    }
    
    @Override
    public boolean beforeInvoke(SqlSession dao, AspectData aspectData, SimpleAspectWrapper simpleWrapper,
        QueryEnumType queryEnumType)
    {
        if (aspectData == null)
            return true;
        JsParser parser = getJsParser();
        return parser.parse(aspectData, simpleWrapper, null, "before");
    }
    
}
