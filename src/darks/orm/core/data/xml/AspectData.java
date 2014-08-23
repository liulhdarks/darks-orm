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

package darks.orm.core.data.xml;

public class AspectData implements Cloneable
{
    
    public enum AspectType
    {
        JYTHON, PYFILE, JAVACLASS, JAVASCRIPT, JSFILE
    }
    
    private AspectType aspectType;
    
    private String className;
    
    private String content;
    
    public AspectData()
    {
        
    }
    
    public AspectType getAspectType()
    {
        return aspectType;
    }
    
    public void setAspectType(AspectType aspectType)
    {
        this.aspectType = aspectType;
    }
    
    public String getClassName()
    {
        return className;
    }
    
    public void setClassName(String className)
    {
        this.className = className;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
        this.content = content;
    }
    
    public AspectData clone()
    {
        try
        {
            return (AspectData)super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
}