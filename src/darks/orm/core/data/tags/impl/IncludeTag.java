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

package darks.orm.core.data.tags.impl;

import java.util.List;

import org.dom4j.Element;

import darks.orm.core.config.sqlmap.SqlMapConfiguration;
import darks.orm.core.data.tags.AbstractTag;
import darks.orm.core.data.xml.InterfaceMethodData;
import darks.orm.exceptions.ConfigException;

public class IncludeTag extends AbstractTag
{

	private String refId;

	private SqlMapConfiguration sqlMapConfig;

	public IncludeTag()
	{
	}

	public IncludeTag(String refId, SqlMapConfiguration sqlMapConfig, AbstractTag prevTag)
	{
		super(prevTag);
		this.refId = refId;
		this.sqlMapConfig = sqlMapConfig;
	}

	@Override
	public Object computeSql(StringBuilder sqlBuf, List<Object> params, InterfaceMethodData data, Object prevValue)
		throws Exception
	{
		AbstractTag tag = sqlMapConfig.getTag(refId);
		if (tag == null)
		{
			throw new ConfigException("Sqlmap include '" + refId + "' does not exist");
		}
		tag.computeSql(sqlBuf, params, data, prevValue);
		return null;
	}

	@Override
	public boolean parseElement(Element el)
	{
		refId = el.attributeValue("refid");
		return refId != null && !"".equals(refId.trim());
	}

	public String getRefId()
	{
		return refId;
	}

	public void setRefId(String refId)
	{
		this.refId = refId;
	}

	public SqlMapConfiguration getSqlMapConfig()
	{
		return sqlMapConfig;
	}

	public void setSqlMapConfig(SqlMapConfiguration sqlMapConfig)
	{
		this.sqlMapConfig = sqlMapConfig;
	}

}
