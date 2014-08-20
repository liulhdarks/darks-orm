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
 * Indicates the entity class inline query annotations.
 * 
 * <p>
 * <h1>Query.java</h1>
 * <p>
 * 
 * When we need to directly through the entity object query or related objects,
 * you can use the notes, so that developers do not have again from other object
 * method to obtain the objects.For example, in the WEB development, in order to
 * keep HTML simple maintenance, we often use label form to process JAVA
 * objects. But using the label form not easy to other classes of objects for
 * further processing, then you can directly from the entity object called Query
 * annotation method, thus to obtain the object.It used to be lazy loading
 * mechanism.
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/01/2012
 * @since JDK1.5
 * @see Param
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Query
{
    
    /**
     * Query result type
     */
    public enum QueryType
    {
        
        /**
         * List result type
         */
        ListType,
        
        /**
         * single object result type
         */
        SingleType
    };
    
    /**
     * SQL query sentence
     */
    String SQL() default "";
    
    /**
     * query parameters array
     */
    String[] paramType() default {};
    
    /**
     * result class of method
     */
    Class<?> resultType();
    
    /**
     * Query result type
     * 
     * @see QueryType
     */
    QueryType queryType() default QueryType.ListType;
}
