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

package darks.orm.util;

public class StringHelper
{
    
    /**
     * 去除每行无效字符
     * 
     * @param s 字符串
     * @return
     */
    public static String lineTrim(String s)
    {
        return lineTrim(s, null);
    }
    
    /**
     * 去除每行无效字符
     * 
     * @param s 字符串
     * @return
     */
    public static String lineTrim(String s, String lineEx)
    {
        int len = s.length();
        int st = 0;
        int start = 0;
        
        while ((st < len))
        {
            if (s.charAt(st) >= ' ')
            {
                break;
            }
            else if (s.charAt(st) == '\n' || s.charAt(len - 1) == '\r')
            {
                start = st + 1;
            }
            st++;
        }
        
        while ((st < len))
        {
            if (s.charAt(len - 1) <= ' ')
                len--;
            else
                break;
        }
        s = s.substring(start, len);
        
        String[] args = null;
        if (s.indexOf("\n") >= 0)
        {
            args = s.split("\n");
        }
        else
        {
            args = new String[] {s};
        }
        st = 0;
        for (int i = 0; i < args[0].length(); i++)
        {
            if (s.charAt(i) <= ' ')
            {
                st++;
            }
            else
            {
                break;
            }
        }
        StringBuffer buf = new StringBuffer();
        for (String line : args)
        {
            line = line.substring(st);
            if (lineEx != null)
            {
                buf.append(lineEx);
            }
            buf.append(line);
            buf.append("\n");
        }
        return buf.toString();
    }
    
    /**
     * 大写首字符
     * 
     * @param type 字符串
     * @return
     */
    public static String upperHeadWord(String type)
    {
        String head = type.substring(0, 1);
        return type.replaceFirst(head, head.toUpperCase());
    }
}