package com.jupitertools.springdynamicpropertyresolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link IncludeDynamicProperty}
 *
 * @author Anatoliy Korovin
 */
class IncludeDynamicTestPropertyTest {

    private ConfigurableApplicationContext mockContext;
    private ConfigurableEnvironment mockEnv;

    @BeforeEach
    void setUp() {
        mockContext = mock(ConfigurableApplicationContext.class);
        mockEnv = new MockEnvironment();
        when(mockContext.getEnvironment()).thenReturn(mockEnv);
    }

    @Test
    void injectPropertyByAnnotationWithClasses() {
        // Arrange
        DynamicTestPropertyContextCustomizerFactory factory =
                new DynamicTestPropertyContextCustomizerFactory();

        // Act
        factory.createContextCustomizer(InjectPropertyThroughAnnotation.class, null)
               .customizeContext(mockContext, null);
        // Assert
        assertThat(mockEnv.getProperty("key")).isEqualTo("051187");
    }

    @Test
    void injectPropertyOnFewLevels() {
        // Arrange
        DynamicTestPropertyContextCustomizerFactory factory =
                new DynamicTestPropertyContextCustomizerFactory();

        // Act
        factory.createContextCustomizer(InjectPropertyOnFewLevels.class, null)
               .customizeContext(mockContext, null);
        // Assert
        assertThat(mockEnv.getProperty("key")).isEqualTo("051187");
        assertThat(mockEnv.getProperty("secret")).isEqualTo("12345");
        assertThat(mockEnv.getProperty("location")).isEqualTo("48.479181,135.098081");
    }

    @Test
    void injectPropertyRepeatable() {
        // Arrange
        DynamicTestPropertyContextCustomizerFactory factory =
                new DynamicTestPropertyContextCustomizerFactory();

        // Act
        factory.createContextCustomizer(InjectPropertyRepeatable.class, null)
               .customizeContext(mockContext, null);
        // Assert
        assertThat(mockEnv.getProperty("key")).isEqualTo("051187");
        assertThat(mockEnv.getProperty("secret")).isEqualTo("12345");
    }

    private static class FirstPropertyClass {

        @DynamicTestProperty
        private static TestPropertyValues getProps() {
            return TestPropertyValues.of("key=051187");
        }
    }

    private static class SecondPropertyClass {

        @DynamicTestProperty
        private static TestPropertyValues getProps() {
            return TestPropertyValues.of("secret=12345");
        }
    }

    @IncludeDynamicProperty(FirstPropertyClass.class)
    private static class InjectPropertyThroughAnnotation {

    }

    @IncludeDynamicProperty(SecondPropertyClass.class)
    private static class InjectPropertyOnFewLevels extends InjectPropertyThroughAnnotation {

        @DynamicTestProperty
        static TestPropertyValues localProperty(){
            return TestPropertyValues.of("location=48.479181,135.098081");
        }
    }

    @IncludeDynamicProperty(SecondPropertyClass.class)
    @IncludeDynamicProperty(FirstPropertyClass.class)
    private static class InjectPropertyRepeatable {

    }
}
