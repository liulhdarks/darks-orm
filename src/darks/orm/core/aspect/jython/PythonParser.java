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

package darks.orm.core.aspect.jython;

import darks.orm.app.QueryEnumType;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.SimpleAspectWrapper;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.core.data.xml.AspectData.AspectType;
import darks.orm.core.factory.JythonFactory;
import darks.orm.exceptions.JythonAspectException;
import org.python.core.PyNone;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class PythonParser
{
    
    public static String JY_ASPECT_DATA = "__DATA";
    
    /**
     * Parse python script data
     * 
     * @param aspectData Aspect data
     * @param simpleWrapper {@linkplain darks.orm.core.data.xml.SimpleAspectWrapper SimpleAspectWrapper}
     * @param queryEnumType {@linkplain darks.orm.app.QueryEnumType QueryEnumType}
     * @param methodType 解析方法类型 JY_ASPECT_BEFORE or JY_ASPECT_AFTER
     * @return 是否继续执行
     */
    public boolean parse(AspectData aspectData, SimpleAspectWrapper simpleWrapper, QueryEnumType queryEnumType,
        String methodType)
        throws JythonAspectException
    {
        if (aspectData == null)
            return true;
        PythonInterpreter pyInter = initPythonInterpreter(simpleWrapper);
        if (aspectData.getAspectType() == AspectType.JYTHON)
        {
            String content = PythonBuilder.buildJython(aspectData, aspectData.getClassName(), aspectData.getContent());
            try
            {
                pyInter.exec(content);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new JythonAspectException(e);
            }
        }
        else if (aspectData.getAspectType() == AspectType.PYFILE)
        {
            pyInter.exec(PythonBuilder.getPythonHeader().toString());
            pyInter.execfile(aspectData.getContent());
            pyInter.exec(PythonBuilder.buildJythonTail(aspectData.getClassName()));
        }
        else
        {
            return true;
        }
        PyObject aspectObj = pyInter.get(PythonBuilder.JY_ASPECT_CLASS);
        PyObject retObj = aspectObj.invoke(methodType);
        
        if (retObj == null || retObj instanceof PyNone)
        {
            return false;
        }
        else
        {
            Integer ret = (Integer)JythonFactory.getInstance().pyObjectToObject(retObj, Integer.class);
            if (ret == 0)
                return false;
            else
                return true;
        }
        
    }
    
    /**
     * 初始化Python引擎并获得PythonInterpreter
     * 
     * @param queryData 查询数据
     * @param sql SQL语句
     * @param values 选择值
     * @param params 参数
     * @param page 当前页
     * @param pageSize 分页大小
     * @return PythonInterpreter 实例
     */
    public PythonInterpreter initPythonInterpreter(SimpleAspectWrapper simpleWrapper)
    {
        PythonInterpreter pyInter = JythonFactory.getInstance().getInterpreter();
        if (simpleWrapper instanceof QueryAspectWrapper)
        {
            pyInter.set(JY_ASPECT_DATA, (QueryAspectWrapper)simpleWrapper);
        }
        else
        {
            pyInter.set(JY_ASPECT_DATA, simpleWrapper);
        }
        return pyInter;
    }
    
}