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
 * Indicates the entity class attribute is a primary key column in database
 * table
 * <p>
 * For example:
 * 
 * <pre>
 * &#064;Id
 * &#064;Column(&quot;USER_Id&quot;)
 * private int userId;
 * </pre>
 * 
 * Generated primary key value mode defaults to automatic database processing,
 * it can also be specified to sequence or use SQL query way of obtaining
 * primary key value.
 * 
 * <pre>
 * &#064;Id(type = GenerateKeyType.SEQUENCE, seq = &quot;SEQ_USER.NEXTVAL()&quot;)
 * &#064;Column(&quot;USER_Id&quot;)
 * private int userId;
 * </pre>
 * 
 * If you want to generate a primary key value.
 * 
 * <pre>
 * &#064;Id(feedBackKey = FeedBackKeyType.SELECT, select = &quot;SELECT LAST_INSERT_ID FROM T_USER&quot;)
 * &#064;Column(&quot;USER_Id&quot;)
 * private int userId;
 * </pre>
 * <p>
 * <h1>Id.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/01/2012
 * @since JDK1.5
 * @see Entity
 * @see Column
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id
{
    
    /**
     * Indicates the obtaining the generated primary key value feedback.
     */
    public enum FeedBackKeyType
    {
        /**
         * No feedback value
         */
        NONE,
        /**
         * Using SQL query feedback value
         */
        SELECT,
        /**
         * Using JDBC GeneratedKey feedback value
         */
        GENERATEDKEY
    }
    
    /**
     * Indicates that Generated primary key value way.
     */
    public enum GenerateKeyType
    {
        /**
         * Responsible for the automatic generation of primary key values from
         * the database.Such as mysql.
         */
        AUTO,
        /**
         * Using sequence generated key value.Such as oracle.
         */
        SEQUENCE,
        /**
         * Using SQL query generated key value.
         */
        SELECT
    }
    
    /**
     * Default no use
     */
    String value() default "";
    
    /**
     * Generate primary key type
     * 
     * @see GenerateKeyType
     */
    GenerateKeyType type() default GenerateKeyType.AUTO;
    
    /**
     * If {@link GenerateKeyType type} is set to SEQUENCE, seq must set database
     * sequence.
     */
    String seq() default "";
    
    /**
     * obtaining the generated primary key value feedback type.
     * 
     * @see FeedBackKeyType
     */
    FeedBackKeyType feedBackKey() default FeedBackKeyType.NONE;
    
    /**
     * If {@link GenerateKeyType type} is set to SELECT or
     * {@link FeedBackKeyType feedBackKey} is set to SELECT, select must set a
     * SQL query to generate a key value.
     */
    String select() default "";
}