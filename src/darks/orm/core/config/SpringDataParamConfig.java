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

public class SpringDataParamConfig
{
	private String resultSetType;
    
    private String concurrency;
    
    private String sensitive;
    
    private boolean autoCommit;
    
    public SpringDataParamConfig()
    {
    	resultSetType = "scroll";
    	concurrency = "read";
    	sensitive = "false";
    	autoCommit = true;
    }

	public String getResultSetType()
	{
		return resultSetType;
	}

	public void setResultSetType(String resultSetType)
	{
		this.resultSetType = resultSetType;
	}

	public String getConcurrency()
	{
		return concurrency;
	}

	public void setConcurrency(String concurrency)
	{
		this.concurrency = concurrency;
	}

	public String getSensitive()
	{
		return sensitive;
	}

	public void setSensitive(String sensitive)
	{
		this.sensitive = sensitive;
	}

	public boolean isAutoCommit()
	{
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit)
	{
		this.autoCommit = autoCommit;
	}

	@Override
	public String toString()
	{
		return "SpringDataParamConfig [resultSetType=" + resultSetType + ", concurrency="
				+ concurrency + ", sensitive=" + sensitive + ", autoCommit=" + autoCommit + "]";
	}
	
    
}
