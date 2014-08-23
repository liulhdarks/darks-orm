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

package darks.orm.core.filter;

import java.lang.reflect.Method;

import darks.orm.annotation.ManyToOne;
import darks.orm.annotation.OneToMany;
import darks.orm.annotation.OneToOne;
import darks.orm.annotation.Query;

import net.sf.cglib.proxy.CallbackFilter;

public class BeanCallbackFilter implements CallbackFilter
{
    
    public int accept(Method method)
    {
        
        if (method.isAnnotationPresent(OneToMany.class))
        {
            return 0;
        }
        if (method.isAnnotationPresent(OneToOne.class) || method.isAnnotationPresent(ManyToOne.class))
        {
            return 1;
        }
        if (method.isAnnotationPresent(Query.class))
        {
            return 2;
        }
        return 3;
    }
    
}