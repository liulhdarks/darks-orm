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
 * Indicates the One to many relation mapping model
 * <p>
 * <h1>OneToMany</h1>
 * <p>
 * For example:
 * <p>
 * If you use the &quot;semi-automatic&quot; form, the need to manually write
 * SQL statements
 * 
 * <pre>
 *      &#064;@OneToMany(resultType=User.class,SQL=&quot;select * from t_user where user_depart_id = ?&quot;)
 *      public List<User> getUsers() {
 *           return users;
 *      }
 * </pre>
 * 
 * If you use the &quot;automation&quot; form, only need to specify the target
 * class foreign key attribute
 * 
 * <pre>
 * &#064;OneToMany(resultType = User.class, mappedBy = &quot;userDepart&quot;, mappedType = MappedType.EntityType)
 * public List&lt;User&gt; getUsers() {
 *      return users;
 * }
 * </pre>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/01/2012
 * @since JDK1.5
 * @see ManyToOne
 * @see OneToOne
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OneToMany
{
    
    /**
     * If you use the &quot;automation&quot; form, need to set the mappedBy to
     * the foreign key attribute of target class. If not, does not need the
     * assignment.
     */
    String mappedBy() default "";
    
    /**
     * If you use the &quot;semi-automatic&quot; form, need to write SQL query
     * statement. If not, does not need the assignment.
     */
    String SQL() default "";
    
    /**
     * The method returns the class type
     */
    Class<?> resultType();
    
    /**
     * Relation mapping types. &quot;automation&quot; or
     * &quot;semi-automatic&quot;
     * 
     * @see MappedType
     */
    MappedType mappedType() default MappedType.SqlType;
}