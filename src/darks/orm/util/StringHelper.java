/**
 * 类名:StringHelper.java
 * 作者:刘力华
 * 创建时间:2012-5-30
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
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
