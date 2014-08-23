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

package darks.orm.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class InternalRequestHolder implements RequestHolder
{
    
    public static final ThreadLocal<ServletRequestAttributes> requestThreadLocal =
        new ThreadLocal<ServletRequestAttributes>();
    
    public static InternalRequestHolder instance = null;
    
    public static InternalRequestHolder getInstance()
    {
        if (instance == null)
        {
            instance = new InternalRequestHolder();
        }
        return instance;
    }
    
    private InternalRequestHolder()
    {
        
    }
    
    @Override
    public HttpServletRequest getRequest()
    {
        ServletRequestAttributes attributes = requestThreadLocal.get();
        if (attributes == null)
            return null;
        return attributes.getRequest();
    }
    
    @Override
    public HttpSession getSession()
    {
        ServletRequestAttributes attributes = requestThreadLocal.get();
        if (attributes == null)
            return null;
        return attributes.getSession(true);
    }
    
    public void setRequest(HttpServletRequest request)
    {
        if (request == null)
            return;
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        requestThreadLocal.set(attributes);
    }
    
    public void destory()
    {
        ServletRequestAttributes attributes = requestThreadLocal.get();
        if (attributes != null)
        {
            attributes.requestCompleted();
            requestThreadLocal.set(null);
        }
    }
    
    public void reset()
    {
        requestThreadLocal.set(null);
    }
}