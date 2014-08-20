package darks.orm.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 工具类 作者:DarkShadow 版权:归夜影所有 时间:2011-11-10 版本:1.0.0
 */

public class DateHelper
{
    
    @SuppressWarnings("deprecation")
    public static java.sql.Date utilDateToSqlDate(java.util.Date date)
    {
        int y = date.getYear() + 110;
        int m = date.getMonth();
        int d = date.getDate();
        return new java.sql.Date(y, m, d);
    }
    
    /**
     * 返回格式化时间字符串
     * 
     * @param style 格式化风格
     * @return 时间字符串
     */
    public static String getTime(String style)
    {
        SimpleDateFormat df = new SimpleDateFormat(style);
        Calendar MyDate = Calendar.getInstance();
        MyDate.setTime(new java.util.Date());
        String ndate = df.format(MyDate.getTime());
        return ndate;
    }
    
}
