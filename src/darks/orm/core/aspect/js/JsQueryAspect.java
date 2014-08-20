package darks.orm.core.aspect.js;

import darks.orm.app.SqlSession;
import darks.orm.app.QueryEnumType;
import darks.orm.core.aspect.QueryAspectAdapter;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.exceptions.AspectException;

public class JsQueryAspect extends QueryAspectAdapter
{
    
    private JsParser jsParser = null;
    
    public JsQueryAspect()
    {
        
    }
    
    @Override
    public boolean beforeInvoke(SqlSession dao, AspectData aspectData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (aspectData == null)
            return true;
        JsParser parser = getJsParser();
        return parser.parse(aspectData, queryWrapper, queryEnumType, "before");
    }
    
    @Override
    public boolean afterInvoke(SqlSession dao, AspectData aspectData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (aspectData == null)
            return true;
        JsParser parser = getJsParser();
        return parser.parse(aspectData, queryWrapper, queryEnumType, "after");
    }
    
    private JsParser getJsParser()
    {
        if (jsParser == null)
        {
            jsParser = new JsParser();
        }
        return jsParser;
    }
    
}
