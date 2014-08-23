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

package darks.orm.core.data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MethodData implements Serializable
{
    
    private static final long serialVersionUID = -9219147812448315504L;
    
    public enum MethodEnumType
    {
        None, Query, OneToOne, ManyToOne, OneToMany
    }
    
    private String methodName;
    
    private String methodSimpleName;
    
    private ConcurrentMap<String, Integer> argumentsMap;
    
    private transient Method method;
    
    private MethodEnumType methodEnumType;
    
    public MethodData()
    {
        argumentsMap = new ConcurrentHashMap<String, Integer>();
    }
    
    public int getArgumentIndex(String argName)
    {
        if (argumentsMap.containsKey(argName))
        {
            return argumentsMap.get(argName);
        }
        return -1;
    }
    
    public void addArgument(int index, String argName)
    {
        argumentsMap.put(argName, index);
    }
    
    public String getMethodName()
    {
        return methodName;
    }
    
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }
    
    public ConcurrentMap<String, Integer> getArgumentsMap()
    {
        return argumentsMap;
    }
    
    public void setArgumentsMap(ConcurrentMap<String, Integer> argumentsMap)
    {
        this.argumentsMap = argumentsMap;
    }
    
    public Method getMethod()
    {
        return method;
    }
    
    public void setMethod(Method method)
    {
        this.method = method;
    }
    
    public String getMethodSimpleName()
    {
        return methodSimpleName;
    }
    
    public void setMethodSimpleName(String methodSimpleName)
    {
        this.methodSimpleName = methodSimpleName;
    }
    
    public MethodEnumType getMethodEnumType()
    {
        return methodEnumType;
    }
    
    public void setMethodEnumType(MethodEnumType methodEnumType)
    {
        this.methodEnumType = methodEnumType;
    }
    
}