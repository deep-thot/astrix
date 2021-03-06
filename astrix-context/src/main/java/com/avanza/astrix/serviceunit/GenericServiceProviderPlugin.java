/*
 * Copyright 2014 Avanza Bank AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.avanza.astrix.serviceunit;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import com.avanza.astrix.beans.core.AstrixBeanKey;
import com.avanza.astrix.beans.publish.ApiProviderClass;
import com.avanza.astrix.beans.publish.BeanDefinitionMethod;
import com.avanza.astrix.beans.service.ServiceDefinition;
import com.avanza.astrix.beans.service.ServiceDefinitionSource;
import com.avanza.astrix.provider.core.AstrixApiProvider;
import com.avanza.astrix.versioning.core.AstrixObjectSerializerConfig;
import com.avanza.astrix.versioning.core.ObjectSerializerDefinition;
import com.avanza.astrix.versioning.core.Versioned;

/**
 * 
 * @author Elias Lindholm
 *
 */
public class GenericServiceProviderPlugin implements ServiceProviderPlugin {
	
	@Override
	public List<ExportedServiceBeanDefinition<?>> getExportedServices(ApiProviderClass apiProvider) {
		List<ExportedServiceBeanDefinition<?>> result = new ArrayList<>();
		for (BeanDefinitionMethod<?> beanDefinition : apiProvider.getBeanDefinitionMethods()) {
			if (!beanDefinition.isService()) {
				continue;
			}
			result.add(createExportedServiceBeanDefinition(beanDefinition, apiProvider));
		}
		return result;
	}
	
	private <T> ExportedServiceBeanDefinition<T> createExportedServiceBeanDefinition(BeanDefinitionMethod<T> beanDefinitionMethod, ApiProviderClass apiProvider) {
		boolean usesServiceRegistry = beanDefinitionMethod.usesServiceRegistry();
		ServiceDefinition<T> serviceDefinition = createServiceDefinition(apiProvider, beanDefinitionMethod, beanDefinitionMethod.getBeanKey());
		return new ExportedServiceBeanDefinition<T>(beanDefinitionMethod.getBeanKey(), serviceDefinition, usesServiceRegistry, beanDefinitionMethod.getServiceComponentName());
	}
	

	private <T> ServiceDefinition<T> createServiceDefinition(ApiProviderClass apiProvider, BeanDefinitionMethod<T> serviceDefinitionMethod, AstrixBeanKey<T> beanKey) {
		Class<?> declaringApi = apiProvider.getProviderClass();
		if (!(declaringApi.isAnnotationPresent(Versioned.class) || serviceDefinitionMethod.isVersioned())) {
			return ServiceDefinition.create(ServiceDefinitionSource.create(apiProvider.getName()), 
											beanKey, 
											serviceDefinitionMethod.getServiceConfigClass(), 
											ObjectSerializerDefinition.nonVersioned(), 
											serviceDefinitionMethod.isDynamicQualified());
		}
		if (!apiProvider.isAnnotationPresent(AstrixObjectSerializerConfig.class)) {
			throw new IllegalArgumentException("Illegal api-provider. Api is versioned but provider does not declare a @AstrixObjectSerializerConfig." +
					" providedService=" + serviceDefinitionMethod.getBeanType().getName() + ", provider=" + apiProvider.getProviderClassName());
		} 
		AstrixObjectSerializerConfig serializerConfig = apiProvider.getAnnotation(AstrixObjectSerializerConfig.class);
		return ServiceDefinition.create(ServiceDefinitionSource.create(apiProvider.getName()),
									  beanKey, 
									  serviceDefinitionMethod.getServiceConfigClass(), 
									  ObjectSerializerDefinition.versionedService(serializerConfig.version(), serializerConfig.objectSerializerConfigurer()),
									  serviceDefinitionMethod.isDynamicQualified());
	}

	@Override
	public Class<? extends Annotation> getProviderAnnotationType() {
		return AstrixApiProvider.class;
	}
	
}