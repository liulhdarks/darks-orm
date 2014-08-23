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

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ServletRequestAttributes
{
    public static final String SESSION_MUTEX_ATTRIBUTE = ServletRequestAttributes.class.getName() + ".MUTEX";
    
    private final HttpServletRequest request;
    
    private volatile HttpSession session;
    
    private final Map<String, Object> sessionAttributesToUpdate = new HashMap<String, Object>();
    
    private volatile boolean requestActive = true;
    
    public ServletRequestAttributes(HttpServletRequest request)
    {
        this.request = request;
    }
    
    public void requestCompleted()
    {
        updateAccessedSessionAttributes();
        this.requestActive = false;
    }
    
    public final boolean isRequestActive()
    {
        return this.requestActive;
    }
    
    public final HttpServletRequest getRequest()
    {
        return this.request;
    }
    
    public final HttpSession getSession(boolean allowCreate)
    {
        if (isRequestActive())
        {
            return this.request.getSession(allowCreate);
        }
        
        if ((this.session == null) && (allowCreate))
        {
            throw new IllegalStateException(
                "No session found and request already completed - cannot create new session!");
        }
        return this.session;
    }
    
    public Object getAttribute(String name, int scope)
    {
        if (scope == 0)
        {
            if (!isRequestActive())
            {
                throw new IllegalStateException("Cannot ask for request attribute - request is not active anymore!");
            }
            return this.request.getAttribute(name);
        }
        
        HttpSession session = getSession(false);
        if (session != null)
        {
            try
            {
                Object value = session.getAttribute(name);
                if (value != null)
                {
                    synchronized (this.sessionAttributesToUpdate)
                    {
                        this.sessionAttributesToUpdate.put(name, value);
                    }
                }
                return value;
            }
            catch (IllegalStateException localIllegalStateException)
            {
            }
        }
        return null;
    }
    
    public void setAttribute(String name, Object value, int scope)
    {
        if (scope == 0)
        {
            if (!isRequestActive())
            {
                throw new IllegalStateException("Cannot set request attribute - request is not active anymore!");
            }
            this.request.setAttribute(name, value);
        }
        else
        {
            HttpSession session = getSession(true);
            synchronized (this.sessionAttributesToUpdate)
            {
                this.sessionAttributesToUpdate.remove(name);
            }
            session.setAttribute(name, value);
        }
    }
    
    public void removeAttribute(String name, int scope)
    {
        if (scope == 0)
        {
            if (isRequestActive())
            {
                this.request.removeAttribute(name);
            }
        }
        else
        {
            HttpSession session = getSession(false);
            if (session != null)
            {
                synchronized (this.sessionAttributesToUpdate)
                {
                    this.sessionAttributesToUpdate.remove(name);
                }
                try
                {
                    session.removeAttribute(name);
                }
                catch (IllegalStateException localIllegalStateException)
                {
                }
            }
        }
    }
    
    public Object resolveReference(String key)
    {
        if ("request".equals(key))
        {
            return this.request;
        }
        if ("session".equals(key))
        {
            return getSession(true);
        }
        
        return null;
    }
    
    public String getSessionId()
    {
        return getSession(true).getId();
    }
    
    public Object getSessionMutex()
    {
        Object mutex = session.getAttribute(SESSION_MUTEX_ATTRIBUTE);
        if (mutex == null)
        {
            mutex = session;
        }
        return mutex;
    }
    
    protected void updateAccessedSessionAttributes()
    {
        this.session = this.request.getSession(false);
        
        synchronized (this.sessionAttributesToUpdate)
        {
            if (this.session != null)
            {
                try
                {
                    for (Map.Entry<String, Object> entry : this.sessionAttributesToUpdate.entrySet())
                    {
                        String name = (String)entry.getKey();
                        Object newValue = entry.getValue();
                        Object oldValue = this.session.getAttribute(name);
                        if (oldValue == newValue)
                        {
                            this.session.setAttribute(name, newValue);
                        }
                    }
                }
                catch (IllegalStateException localIllegalStateException)
                {
                }
            }
            this.sessionAttributesToUpdate.clear();
        }
    }
    
    public String toString()
    {
        return this.request.toString();
    }
}