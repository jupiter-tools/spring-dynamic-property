package com.jupitertools.springdynamicpropertyresolver;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class PropertyProviderTest {

	@Test
	void correctPublicStaticProperty() throws NoSuchMethodException {
		// Arrange
		Method method = CorrectVariants.class.getDeclaredMethod("first");
		PropertyProvider provider = new PropertyProvider(method);
		MockEnvironment env = new MockEnvironment();
		// Act
		provider.getTestPropertyValues().applyTo(env);
		// Assert
		assertThat(env.getProperty("a")).isEqualTo("51187");
	}

	@Test
	void correctPrivateStaticProperty() throws NoSuchMethodException {
		// Arrange
		Method method = CorrectVariants.class.getDeclaredMethod("second");
		PropertyProvider provider = new PropertyProvider(method);
		MockEnvironment env = new MockEnvironment();
		// Act
		provider.getTestPropertyValues().applyTo(env);
		// Assert
		assertThat(env.getProperty("b")).isEqualTo("123");
	}

	@Test
	void correctPackageStaticProperty() throws NoSuchMethodException {
		// Arrange
		Method method = CorrectVariants.class.getDeclaredMethod("third");
		PropertyProvider provider = new PropertyProvider(method);
		MockEnvironment env = new MockEnvironment();
		// Act
		provider.getTestPropertyValues().applyTo(env);
		// Assert
		assertThat(env.getProperty("c")).isEqualTo("003");
	}

	@Test
	void throwErrorWhileInvoke() throws NoSuchMethodException {
		// Arrange
		Method method =
				ErrorWhileRetrieveTheValueFromMethodClass.class.getDeclaredMethod("throwsException");
		PropertyProvider provider = new PropertyProvider(method);
		// Act & Assert
		assertThatThrownBy(provider::getTestPropertyValues)
				.isInstanceOf(DynamicTestPropertyException.class)
				.hasMessage("Error while trying to get a value of dynamic properties.");
	}

	@Test
	void testPropertyProviderWithNonStaticMethod() throws NoSuchMethodException {
		// Arrange
		Method method =
				ErrorWhileRetrieveTheValueFromMethodClass.class.getDeclaredMethod("nonStaticMethod");
		// Act & Assert
		assertThatThrownBy(() -> new PropertyProvider(method))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Annotation @DynamicTestProperty must be used on a static method.");
	}

	@Test
	void withoutDynamicPropertyAnnotation() throws NoSuchMethodException {
		// Arrange
		Method method =
				ErrorWhileRetrieveTheValueFromMethodClass.class.getDeclaredMethod("withoutAnnotation");
		// Act & Assert
		assertThatThrownBy(() -> new PropertyProvider(method))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("PropertyProvider must be annotated by @DynamicTestProperty");
	}


	private static class CorrectVariants {

		@DynamicTestProperty
		private static TestPropertyValues first() {
			return TestPropertyValues.of("a=51187");
		}

		@DynamicTestProperty
		public static TestPropertyValues second() {
			return TestPropertyValues.of("b=123");
		}

		@DynamicTestProperty
		static TestPropertyValues third() {
			return TestPropertyValues.of("c=003");
		}
	}

	private static class ErrorWhileRetrieveTheValueFromMethodClass {

		@DynamicTestProperty
		private static TestPropertyValues throwsException() {
			throw new RuntimeException("oops");
		}

		@DynamicTestProperty
		private TestPropertyValues nonStaticMethod() {
			return TestPropertyValues.of("a=123");
		}

		private static TestPropertyValues withoutAnnotation() {
			return TestPropertyValues.of("a=123");
		}
	}
}