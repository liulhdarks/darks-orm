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

package darks.orm.core.data.xml;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import darks.orm.annotation.Param;
import darks.orm.annotation.sqlmap.Select;
import darks.orm.annotation.sqlmap.Select.QueryType;
import darks.orm.annotation.sqlmap.Update;
import darks.orm.app.QueryEnumType;
import darks.orm.exceptions.SessionException;
import darks.orm.util.SqlHelper;
import darks.orm.util.StringHelper;
import darks.orm.util.StringHelper.ParamFlag;

public class InterfaceMethodData
{
	
	public static final String PAGE_PARAM_KEY = "@PAGE$";
	
	public static final String PAGESIZE_PARAM_KEY = "@PAGESIZE$";
	
	public static final String VALUES_PARAM_KEY = "@VALUES$";
    
    private String namespace;
    
    private String sql;
    
    private Class<?> resultClass;
    
    private QueryEnumType queryEnumType;
    
    private String cacheId;
    
    private String attribute;
    
    private String alias;
    
    private boolean autoCache = true;
    
    private boolean autoCascade = true;
    
    private Map<String, Integer> argumentsMap;
    
    public InterfaceMethodData()
    {
        argumentsMap = new HashMap<String, Integer>();
    }
    
    public InterfaceMethodData(Select query, Update update, Method method)
    {
        argumentsMap = new HashMap<String, Integer>();
    	autoCache = true;
        autoCascade = true;
        parseMethodParam(method);
        if (query != null)
        {
        	parseQuery(query);
        }
        else if (update != null)
        {
            sql = update.SQL();
        }
    }
    
    private void parseQuery(Select query)
    {
    	sql = query.SQL();
        resultClass = query.resultType();
        cacheId = query.cacheId();
        attribute = query.attribute();
        alias = query.alias();
        autoCache = query.autoCache();
        autoCascade = query.autoCascade();
        if (resultClass == null)
        {
        	throw new SessionException("Query result class is null");
        }
        QueryType queryType = query.queryType();
        if (queryType == QueryType.SingleType)
        {
        	queryEnumType = QueryEnumType.Object;
        }
        else if (queryType == QueryType.PageType)
        {
        	queryEnumType = QueryEnumType.Page;
        }
        else
        {
        	queryEnumType = QueryEnumType.List;
        }
        String valuesName = query.values();
        String pageName = query.page();
        String pageSizeName = query.pageSize();
        addExternArgument(valuesName, VALUES_PARAM_KEY);
        addExternArgument(pageName, PAGE_PARAM_KEY);
        addExternArgument(pageSizeName, PAGESIZE_PARAM_KEY);
    }
    
    public int addExternArgument(String argName, String key)
    {
        ParamFlag param = StringHelper.parseParamFlag(argName);
        int index = addExternArgument(param, key);
		if (index < 0)
		{
			throw new SessionException("Invalid sqlmap query param tag's value " + param.name);
		}
		return index;
    }
    
    public int addExternArgument(ParamFlag param, String key)
    {
    	int index = -1;
        if (param != null)
        {
        	index = param.index;
        	if (param.type == ParamFlag.TYPE_NAME || param.index < 0)
        	{
        		index = getArgumentIndex(param.name);
        	}
    		if (index >= 0)
    		{
            	addArgument(index, key);
    		}
        }
        return index;
    }
    
    private void parseMethodParam(Method method)
    {
    	try
		{
			for (int i = 0; i < method.getParameterTypes().length; i++)
			{
				if (method.getParameterAnnotations()[i].length == 0)
					continue;
				Annotation ap = method.getParameterAnnotations()[i][0];
				if (ap.annotationType().equals(Param.class))
				{
					Param param = (Param) ap;
					addArgument(i, param.value());
				}
			}
		}
		catch (Exception e)
		{
			throw new SessionException(e.getMessage(), e);
		}
    }
    
    public int getArgumentIndex(String argName)
    {
    	Integer result = argumentsMap.get(argName);
        if (result != null)
        {
            return result;
        }
        return -1;
    }
    
    public void addArgument(int index, String argName)
    {
        argumentsMap.put(argName, index);
    }
    
    public Map<String, Integer> getArgumentsMap()
    {
        return argumentsMap;
    }
    
    public String getNamespace()
    {
        return namespace;
    }
    
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }
    
    public String getSql()
    {
        return sql;
    }
    
    public void setSql(String sql)
    {
        this.sql = sql;
    }
    
    public Class<?> getResultClass()
    {
        return resultClass;
    }
    
    public void setResultClass(Class<?> resultClass)
    {
        this.resultClass = resultClass;
    }
    
    public QueryEnumType getQueryEnumType()
    {
        return queryEnumType;
    }
    
    public void setQueryEnumType(QueryEnumType queryEnumType)
    {
        this.queryEnumType = queryEnumType;
    }
    
    public String getCacheId()
    {
        return cacheId;
    }
    
    public void setCacheId(String cacheId)
    {
        this.cacheId = cacheId;
    }
    
    public String getAttribute()
    {
        return attribute;
    }
    
    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }
    
    public String getAlias()
    {
        return alias;
    }
    
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
    
    public boolean isAutoCache()
    {
        return autoCache;
    }
    
    public void setAutoCache(boolean autoCache)
    {
        this.autoCache = autoCache;
    }
    
    public boolean isAutoCascade()
    {
        return autoCascade;
    }
    
    public void setAutoCascade(boolean autoCascade)
    {
        this.autoCascade = autoCascade;
    }
    
}