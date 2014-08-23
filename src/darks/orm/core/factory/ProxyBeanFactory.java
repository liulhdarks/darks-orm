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

package darks.orm.core.factory;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import darks.orm.app.SqlSession;
import darks.orm.exceptions.ClassReflectException;
import darks.orm.core.filter.BeanCallbackFilter;
import darks.orm.core.interceptor.NoOpMethodInterceptor;
import darks.orm.core.interceptor.OneToManyInterceptor;
import darks.orm.core.interceptor.QueryMethodInterceptor;
import darks.orm.core.interceptor.AnyToOneInterceptor;
import darks.orm.core.interceptor.SqlMapInterceptor;
import darks.orm.util.ReflectHelper;

@SuppressWarnings("unchecked")
public class ProxyBeanFactory
{
    
    private static final OneToManyInterceptor oneToManyInterceptorImpl = new OneToManyInterceptor();
    
    private static final AnyToOneInterceptor someToOneInterceptorImpl = new AnyToOneInterceptor();
    
    private static final QueryMethodInterceptor queryMethodnterceptorImpl = new QueryMethodInterceptor();
    
    private static final NoOpMethodInterceptor noOpMethodInterceptor = new NoOpMethodInterceptor();
    
    private static final BeanCallbackFilter filter = new BeanCallbackFilter();
    
    public ProxyBeanFactory()
    {
        
    }
    
    /**
     * 获得带力实体类
     * 
     * @param <T> 类范型
     * @param c 实体类
     * @return 类实体
     */
    public static <T> T getProxyEntity(Class<T> c)
        throws ClassReflectException
    {
        
        Callback[] callbacks =
            new Callback[] {oneToManyInterceptorImpl, someToOneInterceptorImpl, queryMethodnterceptorImpl,
                noOpMethodInterceptor};
        return (T)Enhancer.create(c, null, filter, callbacks);
    }
    
    public static <T> T getSqlMapProxy(Class<T> clazz, SqlSession session)
    {
        if (ReflectHelper.isInterfaceClass(clazz))
            return (T)Enhancer.create(null, new Class[] {clazz}, new SqlMapInterceptor(session, clazz));
        else
            return (T)Enhancer.create(clazz, null, new SqlMapInterceptor(session, clazz));
    }
}