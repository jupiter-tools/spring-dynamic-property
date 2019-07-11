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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.platform.commons.util.AnnotationUtils;

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

		processDynamicPropertyAnnotation(testClass);
		processIncludeDynamicPropertyAnnotation(testClass);
		return propertyProviders.isEmpty() ? new DynamicTestPropertyContextCustomizer(Collections.emptySet())
		                                   : new DynamicTestPropertyContextCustomizer(propertyProviders);
	}

	private void processDynamicPropertyAnnotation(Class<?> testClass){
		ReflectionUtils.doWithMethods(testClass, this::getPropertySupplierFromMethod);
	}

	private void processIncludeDynamicPropertyAnnotation(Class<?> testClass) {
		//This is AnnotationUtils from junit5
		// because this version works fine with repeatable and inherited
		AnnotationUtils.findRepeatableAnnotations(testClass, IncludeDynamicProperty.class)
					   .stream()
					   .map(IncludeDynamicProperty::value)
					   .flatMap(Stream::of)
					   .forEach(this::processDynamicPropertyAnnotation);
	}

	private void getPropertySupplierFromMethod(Method method) {
		if (method.isAnnotationPresent(DynamicTestProperty.class)) {
			propertyProviders.add(new PropertyProvider(method));
		}
	}

}
