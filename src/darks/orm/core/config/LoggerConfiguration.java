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

package darks.orm.core.config;

public class LoggerConfiguration
{
    
    private boolean showLog = false;
    
    private boolean showSql = false;
    
    private boolean writeLog = false;
    
    public LoggerConfiguration()
    {
        
    }
    
    public boolean isShowLog()
    {
        return showLog;
    }
    
    public void setShowLog(boolean showLog)
    {
        this.showLog = showLog;
    }
    
    public boolean isShowSql()
    {
        return showSql;
    }
    
    public void setShowSql(boolean showSql)
    {
        this.showSql = showSql;
    }
    
    public boolean isWriteLog()
    {
        return writeLog;
    }
    
    public void setWriteLog(boolean writeLog)
    {
        this.writeLog = writeLog;
    }
    
}