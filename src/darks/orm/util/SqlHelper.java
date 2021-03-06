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

import java.lang.reflect.Field;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import darks.orm.core.data.SqlParamData;
import darks.orm.core.data.SqlParamData.SqlParamEnumType;
import darks.orm.util.StringHelper.ParamFlag;

public class SqlHelper
{
    
    /**
     * 重构参数,将参数放到相应的位置
     * 
     * @param sql SQL语句
     * @param params 参数数组
     * @return
     */
    public static Object[] buildParams(String sql, Object[] params)
    {
        if (sql.indexOf('#') < 0)
            return params;
        int index = 0;
        int start = -1;
        int end = -1;
        List<Object> list = new LinkedList<Object>();
        while (index < sql.length())
        {
            char c = sql.charAt(index);
            if (c == '#')
            {
                start = index + 1;
                index++;
                continue;
            }
            if (start < 0)
            {
                index++;
                continue;
            }
            if (c < '0' || c > '9')
            {
                end = index;
            }
            else if (index == sql.length() - 1)
            {
                end = index + 1;
            }
            if (start > 0 && end > 0)
            {
                String p = sql.substring(start, end);
                int i = Integer.parseInt(p);
                list.add(params[i - 1]);
                start = -1;
                end = -1;
            }
            index++;
        }
        return list.toArray();
    }
    
    /**
     * 过滤SQL语句,将#1 #1.userName #user.userName符号替换为?
     * 
     * @param sql SQL语句
     * @return
     */
    public static String filterSql(String sql)
    {
        if (sql == null)
            return null;
        if (sql.indexOf('#') < 0)
            return sql;
        return sql.replaceAll("\\s*#(\\w+\\.?)+\\s*", " ? ");
    }
    
    /**
     * 将Clob转换为字符串
     * 
     * @param clob Clob对象
     * @return Clob内容字符串
     */
    public static String clobToStr(Clob clob)
    {
        try
        {
            return (clob != null ? clob.getSubString(1, (int)clob.length()) : null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 将字符串转换为Clob
     * 
     * @param str Clob内容字符串
     * @return Clob对象
     */
    public static Clob stringToClob(String str)
    {
        if (null == str)
            return null;
        else
        {
            try
            {
                java.sql.Clob c = new javax.sql.rowset.serial.SerialClob(str.toCharArray());
                return c;
            }
            catch (Exception e)
            {
                return null;
            }
        }
    }
    
    /**
     * 解析SQL语句,获得参数数据
     * 
     * @param sql SQL语句
     * @return SQL参数数据
     * @throws Exception
     */
    public static List<SqlParamData> parseSqlParams(String sql)
        throws SQLException
    {
        int start = sql.indexOf('#');
        if (start < 0)
            return null;
        char ch;
        int sqllen = sql.length();
        SqlParamData data = null;
        List<SqlParamData> dataList = new ArrayList<SqlParamData>();
        do
        {
            int etend = start + 1;
            data = new SqlParamData();
            StringBuffer buf = new StringBuffer();
            SqlParamEnumType type = SqlParamEnumType.INDEX; //
            while (true)
            {
                if (etend >= sqllen)
                {
                    if (buf.length() > 0)
                    {
                        data.addLastValue(buf.toString());
                        if (data.getType() == null)
                            data.setType(type);
                    }
                    else
                        throw new SQLException("parseSqlParams sql is invalid");
                    break;
                }
                ch = sql.charAt(etend);
                if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z'))
                {
                    buf.append(ch);
                    type = SqlParamEnumType.ENTITY;
                }
                else if (ch >= '0' && ch <= '9')
                {
                    buf.append(ch);
                }
                else if (ch == '.')
                {
                    data.addLastValue(buf.toString());
                    if (data.getType() == null)
                        data.setType(type);
                    buf = new StringBuffer();
                }
                else
                {
                    data.addLastValue(buf.toString());
                    if (data.getType() == null)
                        data.setType(type);
                    break;
                }
                etend++;
            }
            dataList.add(data);
            start = etend;
            start = sql.indexOf('#', start);
        } while (start >= 0);
        return dataList;
    }
    
    public static final List<Object> buildSqlParams(List<SqlParamData> dataList, Map<String, Integer> argMap,
        Object[] args)
        throws SQLException
    {
        List<Object> list = new LinkedList<Object>();
        String tmp = null;
        Object arg = null;
        Class<?> clazz = null;
        Field field = null;
        for (SqlParamData data : dataList)
        {
            SqlParamData cur = data;
            while (cur != null)
            {
                tmp = cur.getValue();
                if (tmp == null)
                {
                    throw new SQLException("buildSqlParams parameter '" + tmp + "' is null");
                }
                if (arg == null)
                {
                    if (cur.getType() == SqlParamEnumType.INDEX)
                    {
                        int index = Integer.parseInt(tmp) - 1;
                        if (index >= 0)
                            arg = args[index];
                        else
                            throw new SQLException("buildSqlParams index '" + index + "' is wrong");
                    }
                    else if (cur.getType() == SqlParamEnumType.ENTITY)
                    {
                        if (argMap != null && argMap.containsKey(tmp))
                        {
                            int index = argMap.get(tmp);
                            if (index >= 0)
                                arg = args[index];
                        }
                        else
                            throw new SQLException("buildSqlParams parameter '" + tmp + "' does not exist");
                    }
                }
                else
                {
                    clazz = arg.getClass();
                    field = ReflectHelper.getAllField(clazz, tmp);
                    if (field == null)
                        throw new SQLException("buildSqlParams field " + tmp + " does not exists in " + clazz);
                    arg = ReflectHelper.getFieldValue(field, arg);
                    if (arg == null)
                    {
                    	throw new SQLException("buildSqlParams fail to get value in " + clazz);
                    }
                }
                cur = cur.next();
            }
            list.add(arg);
            arg = null;
            tmp = null;
        }
        return list;
    }
    
    public static Object[] buildSqlParams(String sql, Map<String, Integer> argMap, Object[] args)
        throws SQLException
    {
        List<SqlParamData> dataList = parseSqlParams(sql);
        if (dataList == null)
        {
        	dataList = new ArrayList<SqlParamData>();
        }
        List<Object> list = buildSqlParams(dataList, argMap, args);
        return list.toArray();
    }
    
    
    public static Object getParamSubObject(List<Object> params, Map<String, Integer> argumentsMap, String path)
    {
    	ParamFlag flag = StringHelper.parseParamFlag(path);
    	boolean existSub = false;
    	Integer index = 0;
    	if (flag.type == ParamFlag.TYPE_INDEX)
    	{
    		index = flag.index;
    	}
    	else
    	{
    		path = flag.name;
        	if (path.indexOf(".") < 0)
        	{
        		index = argumentsMap.get(path);
        	}
        	else
        	{
            	String[] args = path.split("\\.");
            	index = argumentsMap.get(args[0]);
            	existSub = true;
        	}
    	}
    	if (index == null || index >= params.size() || index < 0)
    	{
    		return null;
    	}
    	Object target = params.get(index);
    	if (existSub)
    	{
    		target = getSubObject(target, path);
    	}
    	return target;
    }
    
    public static Object getSubObject(Object source, String path)
    {
    	String[] args = path.split("\\.");
    	return getSubObject(source, args);
    }
    
    public static Object getSubObject(Object source, String[] args)
    {
    	int index = 0;
    	Object result = source;
    	for (String arg : args)
    	{
    		if ("".equals(arg))
    		{
    			continue;
    		}
    		if (index++ > 0)
    		{
    			if (result == null)
    			{
    				return null;
    			}
    			Field field = ReflectHelper.getAllField(result.getClass(), arg);
    			result = ReflectHelper.getFieldValue(field, result);
    		}
    	}
    	return result;
    }
}
