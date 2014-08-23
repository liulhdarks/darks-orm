/**
 * 
 * Copyright 2014 The Darks ORM Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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