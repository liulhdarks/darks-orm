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

package darks.orm.core.data.tags.express;

import java.util.LinkedList;

/**
 * 逆波兰式运算符接口
 * @author lihua.llh
 *
 */
public interface RPNOper
{

    /**
     * 计算运算符结果（结果需入栈）
     * @param stack 操作数栈
     * @param param 参数
     * @throws ExpressException
     */
	public void compute(LinkedList<Object> stack, Object param) throws ExpressException;
}
