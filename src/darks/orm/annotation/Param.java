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

package darks.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the parameter name of method.It is often used to relation model
 * mapping and SqlMap mapping.
 * <p>
 * <h1>Param.java</h1>
 * <p>
 * For example:
 * 
 * When it is used in SqlMap abstract mapping.
 * 
 * <pre>
 * public abstract void saveUser(@Param(&quot;user&quot;) User user);
 * </pre>
 * 
 * When it is used in relation mapping model {@link Query} annotation.
 * 
 * <pre>
 *      &#064;Query(SQL = "select * from t_user where user_name = ?", 
 *                     paramType = {#userName},
 *                     resultType = User.class)
 *      public List<User> getUsers(@Param(&quot;userName&quot;) String userName) {
 *           ...
 *      }
 * </pre>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/01/2012
 * @since JDK1.5
 * @see Query
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param
{
    
    /**
     * Parameter name.Required.
     */
    public String value() default "";
}
