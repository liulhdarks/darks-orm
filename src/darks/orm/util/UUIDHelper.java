/**
 * 类名:UUIDHelper.java
 * 作者:刘力华
 * 创建时间:2012-5-30
 * 版本:1.0.0 alpha 
 * 版权:CopyRight(c)2012 刘力华  该项目工程所有权归刘力华所有  
 * 描述:
 */

package darks.orm.util;

import java.util.UUID;

public class UUIDHelper
{
    
    public static String getUUID()
    {
        String s = UUID.randomUUID().toString();
        // 去掉“-”符号
        return s.replace("-", "");
    }
    
    public static String[] getUUID(int number)
    {
        if (number < 1)
        {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++)
        {
            ss[i] = getUUID();
        }
        return ss;
    }
}
