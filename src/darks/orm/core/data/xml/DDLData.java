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

public class DDLData
{
    
    public enum DDLType
    {
        Create, Alter
    };
    
    private String id;
    
    private boolean checkTable = true;
    
    private boolean autoRunable = true;
    
    private String sql;
    
    private DDLType type;
    
    private String tableName;
    
    public DDLData()
    {
        
    }
    
    public DDLData(String id, boolean checkTable, String sql, DDLType type)
    {
        super();
        this.id = id;
        this.checkTable = checkTable;
        this.sql = sql;
        this.type = type;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public boolean isCheckTable()
    {
        return checkTable;
    }
    
    public void setCheckTable(boolean checkTable)
    {
        this.checkTable = checkTable;
    }
    
    public String getSql()
    {
        return sql;
    }
    
    public void setSql(String sql)
    {
        this.sql = sql;
    }
    
    public DDLType getType()
    {
        return type;
    }
    
    public void setType(DDLType type)
    {
        this.type = type;
    }
    
    public boolean isAutoRunable()
    {
        return autoRunable;
    }
    
    public void setAutoRunable(boolean autoRunable)
    {
        this.autoRunable = autoRunable;
    }
    
    public String getTableName()
    {
        return tableName;
    }
    
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
}