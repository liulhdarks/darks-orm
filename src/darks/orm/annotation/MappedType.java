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

/**
 * Relation mapping types. &quot;automation&quot; or &quot;semi-automatic&quot;
 * <p>
 * <h1>MappedType.java</h1>
 * <p>
 * 
 * @author Liu LiHua
 * @version 1.0.0 v05/01/2012
 * @since JDK1.5
 */
public enum MappedType
{
    
    /**
     * &quot;automation&quot; relational mapping types
     */
    EntityType,
    /**
     * &quot;semi-automatic&quot; relational mapping types
     */
    SqlType
}