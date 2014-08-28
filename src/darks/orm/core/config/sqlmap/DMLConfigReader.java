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

package darks.orm.core.config.sqlmap;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

import darks.orm.app.QueryEnumType;
import darks.orm.core.aspect.jython.PythonBuilder;
import darks.orm.core.data.tags.AbstractTag;
import darks.orm.core.data.tags.RootTag;
import darks.orm.core.data.tags.TagsFactory;
import darks.orm.core.data.tags.impl.TextTag;
import darks.orm.core.data.xml.AspectData;
import darks.orm.core.data.xml.AspectData.AspectType;
import darks.orm.core.data.xml.DMLData;
import darks.orm.core.data.xml.DMLData.DMLType;
import darks.orm.core.data.xml.DMLQueryData;
import darks.orm.core.data.xml.DMLQueryData.DMLQueryDataType;
import darks.orm.core.data.xml.DMLUpdateData;
import darks.orm.core.factory.TransformFactory;
import darks.orm.exceptions.ConfigException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;
import darks.orm.util.StringHelper;

@SuppressWarnings("unchecked")
public class DMLConfigReader
{

	private static final Logger logger = LoggerFactory.getLogger(DMLConfigReader.class);

	private SqlMapConfiguration sqlMapConfig;

	public DMLConfigReader(SqlMapConfiguration sqlMapConfig)
	{
		this.sqlMapConfig = sqlMapConfig;
	}

	/**
	 * Read sqlmap DML xml element
	 * 
	 * @param element Xml element
	 */
	public void reader(Element element, File file)
	{
		String namesp = element.attributeValue("namespace");
		if (namesp == null)
			namesp = "";
		try
		{
			for (Iterator<Element> it = element.elementIterator(); it.hasNext();)
			{
				Element el = it.next();
				String name = el.getName().trim();
				if ("Query".equalsIgnoreCase(name))
				{
					readQuery(el, file, namesp);
				}
				else if ("Update".equalsIgnoreCase(name))
				{
					readUpdate(el, file, namesp);
				}
			}
		}
		catch (Exception e)
		{
			throw new ConfigException(e.getMessage(), e);
		}
	}

	/**
	 * Read and parse query tags
	 * 
	 * @param element Query tag element
	 * @throws Exception 
	 */
	private DMLData readQuery(Element element, File file, String namesp) throws Exception
	{
		DMLData dmlData = new DMLData();
		dmlData.setType(DMLType.Query);
		DMLQueryData queryData = new DMLQueryData();
		queryData.setQueryType(QueryEnumType.Auto);
		queryData.setAutoCascade(true);
		dmlData.setQueryData(queryData);
		try
		{
			for (Iterator<Attribute> it = element.attributeIterator(); it.hasNext();)
			{
				Attribute attr = it.next();
				readQueryAttribute(attr, dmlData, queryData);
			}
		}
		catch (Exception e)
		{
			throw new ConfigException(e.getMessage(), e);
		}
		if (queryData.getResultClass() == null)
		{
			throw new ConfigException("Sqlmap query " + dmlData.getId() + " loss result class config");
		}
		if (dmlData.getId() == null)
			return null;

		RootTag rootTag = new RootTag();
		parseSqlTag(rootTag, element);
		queryData.setSqlTag(rootTag);
		String sql = element.getTextTrim();
		if (sql != null && !"".equals(sql))
		{
			queryData.setQueryDataType(DMLQueryDataType.Simple);
			queryData.setSQL(sql);
		}
		else
		{
			if (!readConstitute(element, queryData))
			{
				if (!readSelect(element, queryData, ""))
				{
					return null;
				}
			}
		}
		AspectData aspectData = parseAspectXml(element);
		queryData.setAspectData(aspectData);
		if (!"".equals(namesp))
		{
			if (!namesp.endsWith("."))
				namesp += ".";
		}
		sqlMapConfig.addDMLData(namesp + dmlData.getId(), dmlData);
		return dmlData;
	}
	
	private void readQueryAttribute(Attribute attr, DMLData dmlData, DMLQueryData queryData) throws Exception
	{
		String name = attr.getName().trim();
		String value = attr.getValue().trim();
		if ("id".equalsIgnoreCase(name))
		{
			dmlData.setId(value);
		}
		else if ("resultType".equalsIgnoreCase(name))
		{
			queryData.setResultType(value);
			Class<?> cls = TransformFactory.getInstance().stringToEntityClass(value);
			if (cls == null)
			{
				throw new ConfigException(value + " does not exists");
			}
			queryData.setResultClass(cls);
		}
		else if ("autoCascade".equalsIgnoreCase(name))
		{
			if ("true".equalsIgnoreCase(value))
				queryData.setAutoCascade(true);
			else if ("false".equalsIgnoreCase(value))
				queryData.setAutoCascade(false);
		}
		else if ("alias".equalsIgnoreCase(name))
		{
			queryData.setAlias(value);
		}
		else if ("attribute".equalsIgnoreCase(name))
		{
			queryData.setAttribute(value);
		}
		else if ("queryType".equalsIgnoreCase(name))
		{
			if ("object".equalsIgnoreCase(value))
				queryData.setQueryType(QueryEnumType.Object);
			else if ("list".equalsIgnoreCase(value))
				queryData.setQueryType(QueryEnumType.List);
			else if ("page".equalsIgnoreCase(value))
				queryData.setQueryType(QueryEnumType.Page);
			else
				queryData.setQueryType(QueryEnumType.Auto);
		}
		else if ("cache".equalsIgnoreCase(name))
		{
			if ("auto".equalsIgnoreCase(value))
				dmlData.setAutoCache(true);
			else if ("manual".equalsIgnoreCase(value))
				dmlData.setAutoCache(false);
		}
		else if ("cacheId".equalsIgnoreCase(name))
		{
			dmlData.setCacheId(value);
		}
		else if ("values".equalsIgnoreCase(name))
		{
			queryData.setValuesParam(StringHelper.parseParamFlag(value));
		}
		else if ("page".equalsIgnoreCase(name))
		{
			queryData.setPageParam(StringHelper.parseParamFlag(value));
		}
		else if ("pageSize".equalsIgnoreCase(name))
		{
			queryData.setPageSizeParam(StringHelper.parseParamFlag(value));
		}
	}

	/**
	 * Read constitute query
	 * 
	 * @param element query element
	 * @param queryData Query data
	 * @return If success, return true
	 */
	private boolean readConstitute(Element element, DMLQueryData queryData)
	{
		queryData.setQueryDataType(DMLQueryDataType.Constitute);
		Element queryElement = element.element("query");
		Element cstElement = element.element("constitute");
		if (queryElement == null || cstElement == null)
			return false;
		String sql = queryElement.getTextTrim();
		if (sql == null || "".equals(sql))
			return false;
		queryData.setSQL(sql);
		List<Element> items = cstElement.elements("item");
		for (Element item : items)
		{
			String value = item.attributeValue("value");
			if (value == null || "".equals(value))
				continue;
			String exsql = item.getTextTrim();
			if (exsql == null)
				exsql = "";
			// queryData.addSQL(value, sql +" "+ exsql);
			queryData.addSQL(value, exsql);
		}
		return true;
	}

	/**
	 * Read select query
	 * 
	 * @param element query element
	 * @param queryData Query data
	 * @param pVal Parent values
	 * @return If success, return true
	 */
	private boolean readSelect(Element element, DMLQueryData queryData, String pVal)
	{
		queryData.setQueryDataType(DMLQueryDataType.Select);
		List<Element> selects = element.elements("select");
		if (selects == null || selects.size() == 0)
			return false;
		for (Element sel : selects)
		{
			String value = sel.attributeValue("value");
			if (value == null || "".equals(value))
				continue;

			List<Element> chsel = sel.elements("select");
			if (chsel.size() > 0)
			{
				readSelect(sel, queryData, pVal + value + "@");
			}
			else
			{
				String sql = sel.getTextTrim();
				String key = pVal + value;
				if (key.endsWith("@"))
				{
					key = key.substring(0, key.length() - 1);
				}
				queryData.addSQL(key, sql);
			}
		}
		return true;
	}

	/**
	 * Read and parse update tags
	 * 
	 * @param element Update tag
	 */
	private DMLData readUpdate(Element element, File file, String namesp)
	{
		DMLData dmlData = new DMLData();
		String sql = element.getTextTrim();
		dmlData.setType(DMLType.Update);
		DMLUpdateData updateData = new DMLUpdateData();
		updateData.setSql(sql);
		for (Iterator<Attribute> it = element.attributeIterator(); it.hasNext();)
		{
			try
			{
				Attribute at = it.next();
				String name = at.getName().trim();
				String value = at.getValue().trim();
				if ("id".equalsIgnoreCase(name))
				{
					dmlData.setId(value);
				}
			}
			catch (Exception e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		if (dmlData.getId() == null)
			return null;
		AspectData aspectData = parseAspectXml(element);
		updateData.setAspectData(aspectData);
		dmlData.setUpdateData(updateData);
		if (!"".equals(namesp))
		{
			if (!namesp.endsWith("."))
				namesp += ".";
		}
		sqlMapConfig.addDMLData(namesp + dmlData.getId(), dmlData);
		return dmlData;
	}

	/**
	 * Read and parse aspect XMLs file
	 * 
	 * @param element Aspect tags
	 * @return Aspect data
	 */
	private AspectData parseAspectXml(Element element)
	{
		Element aspectEl = element.element("aspect");
		if (aspectEl == null)
			return null;
		AspectData aspectData = new AspectData();
		boolean isNext = false;
		Element jythonEl = aspectEl.element("jython");
		if (jythonEl != null)
		{
			aspectData.setAspectType(AspectType.JYTHON);
			Attribute attr = jythonEl.attribute("className");
			if (attr == null)
				return null;
			aspectData.setClassName(attr.getValue().trim());
			String text = jythonEl.getText();
			if (text == null || "".equals(text.trim()))
			{
				String before = jythonEl.elementText("before");
				String after = jythonEl.elementText("after");
				if (before == null || "".equals(before.trim()))
				{
					if (after == null || "".equals(after.trim()))
						return null;
				}
				text = PythonBuilder.buildBody(aspectData.getClassName(), before, after);
			}
			aspectData.setContent(text);
			isNext = true;
		}

		Element pyfileEl = aspectEl.element("pyfile");
		if (pyfileEl != null && isNext == false)
		{
			aspectData.setAspectType(AspectType.PYFILE);
			Attribute attr = pyfileEl.attribute("className");
			if (attr == null)
				return null;
			aspectData.setClassName(attr.getValue().trim());
			aspectData.setContent(pyfileEl.getTextTrim());
			isNext = true;
		}

		Element javaClassEl = aspectEl.element("javaClass");
		if (javaClassEl != null && isNext == false)
		{
			aspectData.setAspectType(AspectType.JAVACLASS);
			aspectData.setClassName(javaClassEl.getTextTrim());
			isNext = true;
		}

		Element jsEl = aspectEl.element("javascript");
		if (jsEl != null && isNext == false)
		{
			aspectData.setAspectType(AspectType.JAVASCRIPT);
			aspectData.setContent(jsEl.getTextTrim());
			isNext = true;
		}

		Element jsfileEl = aspectEl.element("jsfile");
		if (jsfileEl != null && isNext == false)
		{
			aspectData.setAspectType(AspectType.JSFILE);
			aspectData.setContent(jsfileEl.getTextTrim());
			isNext = true;
		}

		if (isNext)
		{
			return aspectData;
		}
		return null;
	}

	private void parseSqlTag(AbstractTag parent, Element el) throws Exception
	{
		AbstractTag prevTag = null;
		List<Node> list = el.content();
		Iterator<Node> it = list.iterator();
		while (it.hasNext())
		{
			Node node = it.next();
			switch (node.getNodeType())
			{
			case Node.ELEMENT_NODE:
				Element childEl = (Element) node;
				AbstractTag childTag = TagsFactory.createTag(childEl, prevTag);
				if (childTag != null)
				{
					if (childTag.parseElement(childEl))
					{
						prevTag = childTag;
						parent.addChild(childTag);
						parseSqlTag(childTag, childEl);
					}
				}
				break;
			case Node.TEXT_NODE:
				String text = node.getText().replaceAll("\n|\t", " ").trim();
				if (!"".equals(text))
				{
					TextTag tag = new TextTag(text, prevTag);
					parent.addChild(tag);
					prevTag = tag;
				}
				break;
			}
		}
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
