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

package darks.orm.core.aspect;

import darks.orm.app.QueryEnumType;
import darks.orm.app.SqlSession;
import darks.orm.core.aspect.jclass.JavaClassQueryAspect;
import darks.orm.core.aspect.js.JsQueryAspect;
import darks.orm.core.aspect.jython.PythonQueryAspect;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.AspectData.AspectType;
import darks.orm.core.data.xml.QueryAspectWrapper;
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
     * @param aspectData
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
        else if (aspectType == AspectType.JAVASCRIPT || aspectType == AspectType.JSFILE)
        {
            return new JsQueryAspect();
        }
        else
        {
            return null;
        }
    }
    
    public boolean beforeInvoke(SqlSession dao, AspectData aspectData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (queryAspect == null)
            return false;
        return queryAspect.beforeInvoke(dao, aspectData, queryWrapper, queryEnumType);
    }
    
    public boolean afterInvoke(SqlSession dao, AspectData aspectData, QueryAspectWrapper queryWrapper,
        QueryEnumType queryEnumType)
        throws AspectException
    {
        if (queryAspect == null)
            return false;
        return queryAspect.afterInvoke(dao, aspectData, queryWrapper, queryEnumType);
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