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

package darks.orm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the attributes of entity class is mapped to a database table
 * column.
 * <p>
 * <h1>Column.java</h1>
 * <p>
 * If the entity class attributes using the annotation, columns of a database
 * table will be according to the notes column name mapped to corresponding to
 * the attribute, if not, the attribute name will default to the database table
 * name.
 * 
 * For example:
 * 
 * <pre>
 * &#064;Column(&quot;USER_NAME&quot;)
 * private String userName;
 * </pre>
 * 
 * or you can use like
 * 
 * <pre>
 * &#064;Column(value = &quot;USER_NAME&quot;, nullable = true, insertable = false)
 * private String userName;
 * </pre>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/01/2012
 * @since JDK1.5
 * @see Entity
 * @see Id
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column
{
    
    /**
     * database table column name. Required.
     */
    public String value() default "";
    
    /**
     * If true, the database table column can be able to set null otherwise,
     * when saving or update an entity object, it will automatically filter the
     * attribute. default false. Optional.
     */
    public boolean nullable() default false;
    
    /**
     * If true, when the persistence the entity object, entity class attribute
     * will be saved to the database corresponding to the column. Otherwise, the
     * attribute will be ignored or filter. default true. Optional.
     */
    public boolean insertable() default true;
    
    /**
     * If true, when update the entity object, entity class attribute will be
     * updated to the database corresponding to the column. Otherwise, the
     * attribute will be ignored or filter. default true. Optional.
     */
    public boolean updatable() default true;
    
    /**
     * If true, when query the entity object, the value of database table column
     * will be setted to the entity class corresponding to the attribute.
     * Otherwise, the value will be ignored or filter. default true. Optional.
     */
    public boolean queryable() default true;
}