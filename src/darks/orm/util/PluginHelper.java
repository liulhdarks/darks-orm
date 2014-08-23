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

package darks.orm.util;

public class PluginHelper
{
    
    public static boolean isRegJythonPlugin()
    {
        try
        {
            Class<?> cls = Class.forName("org.python.util.PythonInterpreter");
            if (cls == null)
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    public static boolean isRegSpringRequestPlugin()
    {
        try
        {
            Class<?> cls = Class.forName("org.springframework.web.context.request.RequestContextListener");
            if (cls == null)
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
    public static boolean isRegSpringContextPlugin()
    {
        try
        {
            Class<?> cls = Class.forName("org.springframework.context.ApplicationContext");
            if (cls == null)
                return false;
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    
}