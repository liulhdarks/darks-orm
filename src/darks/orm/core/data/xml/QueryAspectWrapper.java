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

public class QueryAspectWrapper extends SimpleAspectWrapper
{
    
    private Object[] values;
    
    private int page;
    
    private int pageSize;
    
    private String alias;
    
    private String attribute;
    
    private int autoCascade;
    
    private Object result;
    
    public QueryAspectWrapper()
    {
        
    }
    
    public QueryAspectWrapper(String sql, Object[] params)
    {
        super(sql, params);
    }
    
    public QueryAspectWrapper(String sql, Object[] params, Object[] values, int page, int pageSize, String alias,
        String attribute, boolean autoCascade)
    {
        super();
        this.sql = sql;
        this.params = params;
        this.values = values;
        this.page = page;
        this.pageSize = pageSize;
        this.alias = alias;
        this.attribute = attribute;
        if (autoCascade)
            this.autoCascade = 1;
        else
            this.autoCascade = 0;
    }
    
    public Object[] getValues()
    {
        return values;
    }
    
    public void setValues(Object[] values)
    {
        if (values == null)
            values = new Object[0];
        this.values = values;
    }
    
    public int getPage()
    {
        return page;
    }
    
    public void setPage(int page)
    {
        this.page = page;
    }
    
    public int getPageSize()
    {
        return pageSize;
    }
    
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }
    
    public String getAlias()
    {
        return alias;
    }
    
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
    
    public String getAttribute()
    {
        return attribute;
    }
    
    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }
    
    public int getAutoCascade()
    {
        return autoCascade;
    }
    
    public void setAutoCascade(int autoCascade)
    {
        this.autoCascade = autoCascade;
    }
    
    public Object getResult()
    {
        return result;
    }
    
    public void setResult(Object result)
    {
        this.result = result;
    }
    
}