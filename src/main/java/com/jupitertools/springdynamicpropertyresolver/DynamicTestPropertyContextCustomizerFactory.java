/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jupitertools.springdynamicpropertyresolver;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.util.ReflectionUtils;

/**
 * {@link ContextCustomizerFactory} to allow using the {@link DynamicTestProperty} in
 * tests.
 *
 * @author Anatoliy Korovin
 */
public class DynamicTestPropertyContextCustomizerFactory
		implements ContextCustomizerFactory {

	private Set<PropertyProvider> propertyProviders = new HashSet<>();

	@Override
	public ContextCustomizer createContextCustomizer(Class<?> testClass,
	                                                 List<ContextConfigurationAttributes> list) {

		ReflectionUtils.doWithMethods(testClass, this::getPropertySupplierFromMethod);

		return propertyProviders.isEmpty() ? null
		                                   : new DynamicTestPropertyContextCustomizer(propertyProviders);
	}

	private void getPropertySupplierFromMethod(Method method) {
		if (method.isAnnotationPresent(DynamicTestProperty.class)) {
			propertyProviders.add(new PropertyProvider(method));
		}
	}

}
