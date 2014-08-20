/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package darks.orm.core.aspect.js;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import darks.orm.app.QueryEnumType;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.QueryAspectWrapper;
import darks.orm.core.data.xml.SimpleAspectWrapper;
import darks.orm.core.data.xml.AspectData.AspectType;
import darks.orm.exceptions.JsAspectException;

/**
 * It is used to parse javascript in sqlmap.
 * 
 * <p>
 * <h1>JsParser.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/03/2012
 * @since JDK1.5
 */
public class JsParser
{
    private static ScriptEngineManager manager;
    
    static
    {
        manager = new ScriptEngineManager();
    }
    
    public boolean parse(AspectData aspectData, SimpleAspectWrapper simpleWrapper, QueryEnumType queryEnumType,
        String methodType)
    {
        if (aspectData == null)
            return true;
        
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        Invocable invoker = (Invocable)engine;
        Object retObj = null;
        try
        {
            if (aspectData.getAspectType() == AspectType.JAVASCRIPT)
            {
                engine.eval(aspectData.getContent());
            }
            else if (aspectData.getAspectType() == AspectType.JSFILE)
            {
                engine.eval(new java.io.FileReader(aspectData.getContent()));
            }
            else
            {
                return true;
            }
            
            if (simpleWrapper instanceof QueryAspectWrapper)
            {
                retObj = invoker.invokeFunction(methodType, (QueryAspectWrapper)simpleWrapper);
            }
            else
            {
                retObj = invoker.invokeFunction(methodType, simpleWrapper);
            }
            
        }
        catch (Exception e)
        {
            throw new JsAspectException("JsParser parse error." + e.toString(), e);
        }
        
        if (retObj == null)
        {
            return false;
        }
        else
        {
            return (Boolean)retObj;
        }
    }
}
