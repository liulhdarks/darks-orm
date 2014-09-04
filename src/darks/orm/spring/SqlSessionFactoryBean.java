/**
 * 
 * Copyright 2014 The Darks ORM Project (Liu lihua)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package darks.orm.spring;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import darks.orm.core.config.SpringDataParamConfig;
import darks.orm.core.factory.SqlSessionFactory;
import darks.orm.datasource.factory.DataSourceFactory;
import darks.orm.exceptions.ConfigException;
import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

/**
 * Sql session factory config for spring.<br>
 * Examples:
 * 
 * <pre>
 * &lt;bean class="darks.orm.spring.SqlSessionFactoryBean"&gt;
 * 		&lt;property name="dataSource" ref="testDataSource" /&gt;
 * 		&lt;property name="scanPackages" value="darks.orm.test.mapper,darks.orm.examples.mapper" /&gt;
 * 		&lt;property name="configLocation" value="classpath:darks.xml" /&gt;
 * 		&lt;property name="dataParamConfig.autoCommit" value="true" /&gt;
 * 		&lt;property name="dataParamConfig.resultSetType" value="scroll" /&gt;
 * 		&lt;property name="dataParamConfig.sensitive" value="false" /&gt;
 * 		&lt;property name="dataParamConfig.concurrency" value="read" /&gt;
 * &lt;/bean&gt;
 * </pre>
 * 
 * @author Liu Lihua
 * 
 */
public class SqlSessionFactoryBean implements BeanDefinitionRegistryPostProcessor,
		InitializingBean, ApplicationContextAware, BeanNameAware
{
	private static Logger log = LoggerFactory.getLogger(SqlSessionFactoryBean.class);

	private DataSource dataSource;

	private Resource configLocation;

	private String scanPackages;

	private boolean scanSubPackage = false;

	private ApplicationContext context;

	private BeanNameGenerator nameGenerator;

	private String beanName;

	private SpringDataParamConfig dataParamConfig = new SpringDataParamConfig();

	public SqlSessionFactoryBean()
	{

	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException
	{
		this.context = context;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException
	{

	}

	@Override
	public void setBeanName(String beanName)
	{
		this.beanName = beanName;
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		if (scanPackages == null || "".equals(scanPackages.trim()))
		{
			throw new ConfigException("Spring scan orm packages is empty");
		}
		if (configLocation == null)
		{
			throw new ConfigException("Spring darks orm configLocation is empty");
		}
		if (dataSource == null)
		{
			throw new ConfigException("Spring darks orm's 'dataSource' is null");
		}
		buildSqlSessionFactory();
	}

	private void buildSqlSessionFactory() throws Exception
	{
		DataSourceFactory.getInstance().setDataSource(dataSource, dataParamConfig);
		SqlSessionFactory.initialize(configLocation.getInputStream());
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
			throws BeansException
	{
		log.debug("DarksORM register scan package by spring.");
		MapperClassDefinitionScanner scanner = new MapperClassDefinitionScanner(registry);
		scanner.setResourceLoader(context);
		scanner.setBeanNameGenerator(nameGenerator);
		scanner.registerFilters();
		String[] packages = scanPackages.trim().split(",");
		scanner.scan(packages);
	}

	public String getScanPackages()
	{
		return scanPackages;
	}

	public void setScanPackages(String scanPackages)
	{
		this.scanPackages = scanPackages;
	}

	public boolean isScanSubPackage()
	{
		return scanSubPackage;
	}

	public void setScanSubPackage(boolean scanSubPackage)
	{
		this.scanSubPackage = scanSubPackage;
	}

	public void setNameGenerator(BeanNameGenerator nameGenerator)
	{
		this.nameGenerator = nameGenerator;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public void setConfigLocation(Resource configLocation)
	{
		this.configLocation = configLocation;
	}

	public SpringDataParamConfig getDataParamConfig()
	{
		return dataParamConfig;
	}

	public void setDataParamConfig(SpringDataParamConfig dataParamConfig)
	{
		this.dataParamConfig = dataParamConfig;
	}

}