
package darks.orm.util;

import java.util.UUID;

public class UUIDHelper
{
    
    public static String getUUID()
    {
        String s = UUID.randomUUID().toString();
        // È¥µô¡°-¡±·ûºÅ
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
