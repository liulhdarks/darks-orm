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

package darks.orm.web.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import darks.orm.web.context.InternalRequestHolder;
import darks.orm.web.context.RequestContext;
import darks.orm.web.context.RequestContext.RequestHolderType;

public class InternalServletRequestListener implements ServletRequestListener
{
    
    public InternalServletRequestListener()
    {
        RequestContext.getInstance().setRequestHolder(RequestHolderType.Internal);
    }
    
    @Override
    public void requestDestroyed(ServletRequestEvent arg0)
    {
        InternalRequestHolder.getInstance().destory();
        
    }
    
    @Override
    public void requestInitialized(ServletRequestEvent requestEvent)
    {
        if (!(requestEvent.getServletRequest() instanceof HttpServletRequest))
        {
            throw new IllegalArgumentException("Request is not an HttpServletRequest: "
                + requestEvent.getServletRequest());
        }
        HttpServletRequest request = (HttpServletRequest)requestEvent.getServletRequest();
        InternalRequestHolder.getInstance().setRequest(request);
    }
    
}