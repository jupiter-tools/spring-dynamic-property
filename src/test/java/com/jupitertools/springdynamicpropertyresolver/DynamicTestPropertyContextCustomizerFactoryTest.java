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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.test.context.ContextCustomizer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link DynamicTestPropertyContextCustomizerFactory}
 *
 * @author Anatoliy Korovin
 */
class DynamicTestPropertyContextCustomizerFactoryTest {

    private DynamicTestPropertyContextCustomizerFactory customizerFactory = new DynamicTestPropertyContextCustomizerFactory();

    @Test
    void singleDynamicTestProperty() throws NoSuchMethodException {
        // Arrange
        Method method = SingleTestPropertyClass.class.getDeclaredMethod("getProps");

        Set<PropertyProvider> providers =
                new HashSet<>(Arrays.asList(new PropertyProvider(method)));

        DynamicTestPropertyContextCustomizer expectedCustomizer =
                new DynamicTestPropertyContextCustomizer(providers);
        // Act
        ContextCustomizer customizer = this.customizerFactory
                .createContextCustomizer(SingleTestPropertyClass.class, null);
        // Assert
        assertThat(customizer).isEqualTo(expectedCustomizer);
    }


    @Test
    void multipleDynamicTestProperty() throws NoSuchMethodException {
        // Arrange
        Method firstMethod = MultipleTestPropertyClass.class.getDeclaredMethod("firstProps");
        Method secondMethod = MultipleTestPropertyClass.class.getDeclaredMethod("secondProps");

        Set<PropertyProvider> providers =
                new HashSet<>(Arrays.asList(new PropertyProvider(firstMethod),
                                            new PropertyProvider(secondMethod)));

        DynamicTestPropertyContextCustomizer expectedCustomizer =
                new DynamicTestPropertyContextCustomizer(providers);

        // Act
        ContextCustomizer customizer = this.customizerFactory
                .createContextCustomizer(MultipleTestPropertyClass.class, null);

        // Assert
        assertThat(customizer).isEqualTo(expectedCustomizer);
    }

    @Test
    void notStaticMethod() {

        ThrowableAssert.ThrowingCallable act = () -> this.customizerFactory
                .createContextCustomizer(ErrorWithNoStaticMethodClass.class, null);

        assertThatThrownBy(act).isInstanceOf(IllegalArgumentException.class)
                               .hasMessage("Annotation @DynamicTestProperty must be used on a static method.");
    }

    @Test
    void wrongReturnTypeOfTheDynamicTestProperty() {

        ThrowableAssert.ThrowingCallable act = () -> this.customizerFactory
                .createContextCustomizer(ErrorWithWrongReturnTypeClass.class, null);

        assertThatThrownBy(act).isInstanceOf(IllegalArgumentException.class)
                               .hasMessage("@DynamicTestProperty method must return the instance of TestPropertyValues.");
    }


    private static class SingleTestPropertyClass {

        @DynamicTestProperty
        private static TestPropertyValues getProps() {
            return TestPropertyValues.of("a=123");
        }

    }

    private static class MultipleTestPropertyClass {

        @DynamicTestProperty
        private static TestPropertyValues firstProps() {
            return TestPropertyValues.of("a=123");
        }

        @DynamicTestProperty
        private static TestPropertyValues secondProps() {
            return TestPropertyValues.of("b=456");
        }

    }

    private static class ErrorWithNoStaticMethodClass {

        @DynamicTestProperty
        private TestPropertyValues getProps() {
            return TestPropertyValues.of("a=123");
        }

    }

    private static class ErrorWithWrongReturnTypeClass {

        @DynamicTestProperty
        private static String getProps() {
            return "a=123";
        }
    }


}
