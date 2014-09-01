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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 采用逆波兰式算法进行表达式解析
 * @author lihua.llh
 *
 */
public class RPNParser
{

	static Map<String, Integer> operlevelMap = new HashMap<String, Integer>();
	
	private static final char VAL_SEG = '\'';
	
	private static final char MIN_CHAR = 32;
    
    private static final String STR_SEG = " ()'|&!=<>+-*/%";
	
	private static final String SPACE = "&nbsp;";
	
	static
	{
		addOperLevel(1, "(", ")");
		addOperLevel(2, "*", "/", "%");
		addOperLevel(3, "+", "-");
		addOperLevel(4, "<", "<=", ">", ">=");
		addOperLevel(5, "==", "!=");
		addOperLevel(6, "&", "&&");
		addOperLevel(7, "|", "||");
	}
	
	private int index;
	
	private LinkedList<String> operStack = new LinkedList<String>();
	
	private LinkedList<String> valQueue = new LinkedList<String>();
    
    private boolean valFlag = false;
	
	private static void addOperLevel(Integer level, String... opers)
	{
		for (String oper : opers)
		{
			operlevelMap.put(oper, level);
		}
	}
	
	public RPNParser()
	{
		reset();
	}
	
	/**
	 * 重置解析器
	 */
	public void reset()
	{
		operStack = new LinkedList<String>();
		valQueue = new LinkedList<String>();
		index = 0;
	}
	
	/**
	 * 解析表达式
	 * @param express 表达式
	 * @return 操作数队列
	 * @throws ExpressException
	 */
	public LinkedList<String> parse(String express) throws ExpressException
	{
		int len = express.length();
		while (index < len)
		{
			char ch = express.charAt(index++);
			if (ch == ' ' || ch == '\t' || ch == '\n' 
					|| ch == '\r' || ch == '\f')
			{
				continue;
			}
			switch (ch)
			{
			case '(':
				operStack.push(String.valueOf(ch));
				break;
			case ')':
				parseToken();
				break;
			case VAL_SEG:
				String val = parseValWithQuot(express).trim();
				valQueue.offer(ExpressParam.STR_FLAG + val);
                validateFlag(true);
				break;
			default:
			    processChars(parseChars(ch, express));
				break;
			}
		}
		doFinal();
        int size = valQueue.size();
        if (1 < size && size < 3)
        {
            throw new ExpressException("Invalid express queue which loss operation." + valQueue);
        }
		return valQueue;
	}
    
    private void validateFlag(boolean actual) throws ExpressException
    {
        if (valFlag == actual)
        {
            throw new ExpressException("Invalid express.repeat value or operation");
        }
        valFlag = actual;
    }
	
	private void doFinal() throws ExpressException
	{
		String op = null;
		while ((op = operStack.peek()) != null)
		{
			op = operStack.pop();
			if ("(".equals(op))
			{
				throw new ExpressException("Invalid RPN express. Loss match token ')'.");
			}
			valQueue.offer(op);
		}
	}
	
	private void processChars(String chars) throws ExpressException
    {
        Integer level = (Integer)operlevelMap.get(chars);
        if (level == null)
        {
        	try
			{
				Integer.parseInt(chars);
				chars = ExpressParam.INT_FLAG + chars;
			}
			catch (Exception e)
			{
				try
				{
					Long.parseLong(chars);
					chars = ExpressParam.LONG_FLAG + chars;
				}
				catch (Exception ex)
				{
				}
			}
            valQueue.offer(chars);
            validateFlag(true);
        }
        else
        {
            processOper(chars, level);
            validateFlag(false);
        }
    }
    
    private void processOper(String oper, Integer level) throws ExpressException
    {
        String op = null;
        while ((op = operStack.peek()) != null)
        {
            if ("(".equals(op))
            {
                break;
            }
            int oldLevel = (Integer)operlevelMap.get(op);
            if (level < oldLevel)
            {
                break;
            }
            operStack.pop();
            valQueue.offer(op);
        }
        operStack.push(oper);
    }
    
    private String parseChars(char ch, String express) throws ExpressException
    {
        int len = express.length();
        if (index < len)
        {
        	char nextCh = express.charAt(index);
            String sch2 = String.valueOf(new char[]{ch, nextCh});
            if (operlevelMap.containsKey(sch2))
            {
            	index++;
            	return sch2;
            }
        }
        String sch = String.valueOf(ch);
        if (operlevelMap.containsKey(sch))
        {
            return sch;
        }
        int start = index;
        StringBuilder buf = new StringBuilder();
        buf.append(ch);
        while (index < len)
        {
            ch = express.charAt(index++);
            if (ch > MIN_CHAR
                    && STR_SEG.indexOf(ch) < 0)
            {
                buf.append(ch);
            }
            else
            {
                index--;
                break;
            }
        }
        String op = buf.toString().trim();
        if ("".equals(op))
        {
            throw new ExpressException("Invalid RPN express near " 
                        + express.substring(Math.max(start - 3, 0), index) + ".Chars is empty.");
        }
        return op;
    }
	
	private void parseToken() throws ExpressException
	{
	    boolean valid = false;
		String op = null;
		while ((op = operStack.peek()) != null)
		{
			op = operStack.pop();
			if ("(".equals(op))
			{
			    valid = true;
				break;
			}
			valQueue.offer(op);
		}
		if (!valid)
		{
		    throw new ExpressException("Invalid RPN express cause loss token '('");
		}
	}
	
	private String parseValWithQuot(String express) throws ExpressException
	{
		int start = index;
		StringBuilder buf = new StringBuilder();
		int len = express.length();
		while (index < len)
		{
			char ch = express.charAt(index++);
			if (VAL_SEG == ch)
			{
				String val = buf.toString();
				if ("".equals(val))
				{
					throw new ExpressException("Invalid RPN express near " 
								+ express.substring(start, index) + ".Value is empty.");
				}
				return val;
			}
			else
			{
				buf.append(ch);
			}
		}
		throw new ExpressException("Invalid RPN express near " 
					+ express.substring(start, index) + ". Loss '\"' char.");
	}
	
	/**
	 * 获得编译字符串
	 * @return 编译字符串
	 */
	public String getCompile()
	{
	    StringBuilder buf = new StringBuilder();
	    for (String val : valQueue)
	    {
	        val = val.replace(" ", SPACE);
	        buf.append(val).append(' ');
	    }
	    buf.setLength(buf.length() - 1);
	    return buf.toString();
	}
	
	/**
	 * 解析编译字符串
	 * @param compile 编译字符串
	 * @return 解析结果
	 */
	public static LinkedList<String> parseCompile(String compile)
	{
	    if (compile == null || "".equals(compile))
	    {
	        return null;
	    }
	    LinkedList<String> linked = new LinkedList<String>();
	    String[] args = compile.split(" ");
	    for (String arg : args)
	    {
	        arg = arg.replace(SPACE, " ");
	        linked.offer(arg);
	    }
	    return linked;
	}

}
