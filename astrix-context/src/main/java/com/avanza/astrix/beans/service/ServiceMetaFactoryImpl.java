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
package com.avanza.astrix.beans.service;

/**
 * 
 * @author Elias Lindholm (elilin)
 *
 */
final class ServiceMetaFactoryImpl implements ServiceMetaFactory {
	
	private final ServiceBeanContext serviceBeanContext;
	
	public ServiceMetaFactoryImpl(ServiceBeanContext serviceBeanContext) {
		this.serviceBeanContext = serviceBeanContext;
	}

	@Override
	public <T> ServiceFactory<T> createServiceFactory(ServiceDefinition<T> serviceDefinition, ServiceDiscoveryFactory<?> serviceDiscoveryFactory) {
		return new ServiceFactory<T>(serviceDefinition, serviceBeanContext, serviceDiscoveryFactory);
	}
	
}
