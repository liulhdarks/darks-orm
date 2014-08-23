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

package darks.orm.annotation.sqlmap;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the SqlMap query method which is used to query SqlMap object list,
 * single object or page object.
 * 
 * <p>
 * <h1>Query.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/01/2012
 * @since JDK1.5
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
         * Object list result type
         */
        ListType,
        
        /**
         * Single object result type
         */
        SingleType,
        
        /**
         * Page object result type
         */
        PageType
    };
    
    /**
     * SQL query sentence.Required.
     */
    String SQL() default "";
    
    /**
     * query result type.Required.
     */
    Class<?> resultType();
    
    /**
     * Query result type.Required.
     * 
     * @see QueryType
     */
    QueryType queryType() default QueryType.ListType;
    
    /**
     * Cache id which is configured in the cache group of configuration file
     */
    String cacheId() default "";
    
    /**
     * The attributes string used in cascade query
     */
    String attribute() default "";
    
    /**
     * The alias string used in cascade query
     */
    String alias() default "";
    
    /**
     * If true, It will automatically use the global cache, if the global cache
     * is enabled in the configuration file.If not, It need to manually
     * configure the caching in the group cache id.
     */
    boolean autoCache() default true;
    
    /**
     * if true, At query time automatically to the entity class injection with
     * information related to the foreign key entity object.However, more
     * complex cascade queries cannot correct injection.If not, The need to
     * manually configure cascade query information, it can handle more complex
     * cascade of query
     */
    boolean autoCascade() default true;
}