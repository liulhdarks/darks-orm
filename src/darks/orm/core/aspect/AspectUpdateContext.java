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

import darks.orm.app.SqlSession;
import darks.orm.app.QueryEnumType;
import darks.orm.core.aspect.jclass.JavaClassUpdateAspect;
import darks.orm.core.aspect.js.JsUpdateAspect;
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
     * @param aspectData
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
        else if (aspectType == AspectType.JSFILE || aspectType == AspectType.JAVASCRIPT)
        {
            return new JsUpdateAspect();
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