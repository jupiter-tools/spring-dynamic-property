package com.jupitertools.springdynamicpropertyresolver;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Tests for {@link DynamicTestPropertyContextCustomizer}
 *
 * @author Anatoliy Korovin
 */
class DynamicTestPropertyContextCustomizerTest {

    private ConfigurableApplicationContext mockContext;
    private ConfigurableEnvironment mockEnv;

    @BeforeEach
    void setUp() {
        mockContext = mock(ConfigurableApplicationContext.class);
        mockEnv = new MockEnvironment();
        when(mockContext.getEnvironment()).thenReturn(mockEnv);
    }

    @Nested
    class CustomizeContextTests {

        @Test
        void customizerWithTheSinglePropertyProvider() throws NoSuchMethodException {
            // Arrange
            Method method = TestClass.class.getDeclaredMethod("singleProperty");
            PropertyProvider provider = new PropertyProvider(method);
            // Act
            new DynamicTestPropertyContextCustomizer(setOf(provider))
                    .customizeContext(mockContext, null);
            // Assert
            assertThat(mockEnv.getProperty("key")).isEqualTo("051187");
        }

        @Test
        void customizerWithMultiplePropertyProvider() throws NoSuchMethodException {
            // Arrange
            Method firstMethod = TestClass.class.getDeclaredMethod("singleProperty");
            Method secondMethod = TestClass.class.getDeclaredMethod("multipleProperties");

            Set<PropertyProvider> providers = setOf(new PropertyProvider(firstMethod),
                                                    new PropertyProvider(secondMethod));
            // Act
            new DynamicTestPropertyContextCustomizer(providers)
                    .customizeContext(mockContext, null);
            // Assert
            assertThat(mockEnv.getProperty("key")).isEqualTo("051187");
            assertThat(mockEnv.getProperty("a")).isEqualTo("1111");
            assertThat(mockEnv.getProperty("b")).isEqualTo("2222");
        }
    }


    @Nested
    class EqualsAndHashCodeTests {

        @Test
        void checkEqualMethods() throws NoSuchMethodException {
            // Arrange
            Method method = TestClass.class.getDeclaredMethod("singleProperty");
            PropertyProvider firstProvider = new PropertyProvider(method);
            PropertyProvider secondProvider = new PropertyProvider(method);

            // Act
            DynamicTestPropertyContextCustomizer firstCustomizer =
                    new DynamicTestPropertyContextCustomizer(setOf(firstProvider));

            DynamicTestPropertyContextCustomizer secondCustomizer =
                    new DynamicTestPropertyContextCustomizer(setOf(secondProvider));

            // Assert
            assertThat(firstCustomizer).isEqualTo(secondCustomizer);
            assertThat(firstCustomizer.hashCode()).isEqualTo(secondCustomizer.hashCode());
        }

        @Test
        void checkNotEqualMethods() throws NoSuchMethodException {
            // Arrange
            Method firstMethod = TestClass.class.getDeclaredMethod("singleProperty");
            PropertyProvider firstProvider = new PropertyProvider(firstMethod);
            Method secondMethod = TestClass.class.getDeclaredMethod("multipleProperties");
            PropertyProvider secondProvider = new PropertyProvider(secondMethod);

            // Act
            DynamicTestPropertyContextCustomizer firstCustomizer =
                    new DynamicTestPropertyContextCustomizer(setOf(firstProvider));

            DynamicTestPropertyContextCustomizer secondCustomizer =
                    new DynamicTestPropertyContextCustomizer(setOf(secondProvider));

            // Assert
            assertThat(firstCustomizer).isNotEqualTo(secondCustomizer);
            assertThat(firstCustomizer.hashCode()).isNotEqualTo(secondCustomizer.hashCode());
        }

        @Test
        void checkEqualsForMethodInBaseClass() throws NoSuchMethodException {
            // Arrange
            Method firstMethod = ReflectionUtils.findMethod(FirstChild.class, "property").get();
            PropertyProvider firstProvider = new PropertyProvider(firstMethod);

            Method secondMethod = ReflectionUtils.findMethod(SecondChild.class, "property").get();
            PropertyProvider secondProvider = new PropertyProvider(secondMethod);

            // Act
            DynamicTestPropertyContextCustomizer firstCustomizer =
                    new DynamicTestPropertyContextCustomizer(setOf(firstProvider));

            DynamicTestPropertyContextCustomizer secondCustomizer =
                    new DynamicTestPropertyContextCustomizer(setOf(secondProvider));

            // Assert
            assertThat(firstCustomizer).isEqualTo(secondCustomizer);
            assertThat(firstCustomizer.hashCode()).isEqualTo(secondCustomizer.hashCode());
        }
    }

    private static Set<PropertyProvider> setOf(PropertyProvider... providers) {
        return new HashSet<>(Arrays.asList(providers));
    }

    private static class TestClass {

        @DynamicTestProperty
        static TestPropertyValues singleProperty() {
            return TestPropertyValues.of("key=051187");
        }

        @DynamicTestProperty
        static TestPropertyValues multipleProperties() {
            return TestPropertyValues.of("a=1111",
                                         "b=2222");
        }
    }

    private static abstract class BaseTest {

        @DynamicTestProperty
        static TestPropertyValues property() {
            return TestPropertyValues.of("key=051187");
        }
    }

    private static class FirstChild extends BaseTest {
    }

    private static class SecondChild extends BaseTest {
    }
}