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

package darks.orm.spring;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import darks.orm.log.Logger;
import darks.orm.log.LoggerFactory;

public class MapperClassDefinitionScanner extends ClassPathBeanDefinitionScanner
{

	private static Logger log = LoggerFactory.getLogger(MapperClassDefinitionScanner.class);

	private BeanDefinitionRegistry registry;

	public MapperClassDefinitionScanner(BeanDefinitionRegistry registry)
	{
		super(registry, false);
		this.registry = registry;
	}

	public void registerFilters()
	{
		addIncludeFilter(new TypeFilter()
		{
			public boolean match(MetadataReader metadataReader,
					MetadataReaderFactory metadataReaderFactory) throws IOException
			{
				boolean ret = metadataReader.getClassMetadata().isAbstract()
						|| metadataReader.getClassMetadata().isInterface();
				return ret;
			}
		});

		addExcludeFilter(new TypeFilter()
		{
			public boolean match(MetadataReader metadataReader,
					MetadataReaderFactory metadataReaderFactory) throws IOException
			{
				String className = metadataReader.getClassMetadata().getClassName();
				return className.endsWith("package-info");
			}
		});
	}

	@Override
	public int scan(String... basePackages)
	{
		int beanCountAtScanStart = this.registry.getBeanDefinitionCount();
		doScan(basePackages);
		AnnotationConfigUtils.registerAnnotationConfigProcessors(this.registry);
		return this.registry.getBeanDefinitionCount() - beanCountAtScanStart;
	}

	@Override
	protected Set<BeanDefinitionHolder> doScan(String... scanPackages)
	{
		Set<BeanDefinitionHolder> definitions = super.doScan(scanPackages);
		if (definitions.isEmpty())
		{
			log.warn("No bean definition found in target packages " + Arrays.toString(scanPackages));
		}
		else
		{
			for (BeanDefinitionHolder holder : definitions)
			{
				GenericBeanDefinition definition = (GenericBeanDefinition) holder
						.getBeanDefinition();
				processBeanDefinition(definition);
			}
		}
		return definitions;
	}

	private void processBeanDefinition(GenericBeanDefinition definition)
	{
		definition.getPropertyValues().add("sqlMapInterface", definition.getBeanClassName());
		definition.setBeanClass(SqlMapFactoryBean.class);
		definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition)
	{
		return ((beanDefinition.getMetadata().isInterface() || beanDefinition.getMetadata().isAbstract()) 
							&& beanDefinition.getMetadata().isIndependent());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition)
			throws IllegalStateException
	{
		if (super.checkCandidate(beanName, beanDefinition))
		{
			return true;
		}
		else
		{
			logger.warn("Skipping MapperFactoryBean with name '" + beanName + "' and '"
					+ beanDefinition.getBeanClassName() + "' mapperInterface"
					+ ". Bean already defined with the same name!");
			return false;
		}
	}

}
