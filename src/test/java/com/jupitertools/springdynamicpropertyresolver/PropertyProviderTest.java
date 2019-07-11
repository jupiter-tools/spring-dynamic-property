package com.jupitertools.springdynamicpropertyresolver;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link PropertyProvider}
 *
 * @author Anatoliy Korovin
 */
class PropertyProviderTest {

	@Nested
	class EvaluateTestPropertyTests {

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
	}

	@Nested
	class ValidationTests {

		@Test
		void throwErrorWhileInvoke() throws NoSuchMethodException {
			// Arrange
			Method method =
					ErrorWhileRetrieveTheValueFromMethodClass.class.getDeclaredMethod("throwsException");
			PropertyProvider provider = new PropertyProvider(method);
			// Act & Assert
			assertThatThrownBy(provider::getTestPropertyValues)
					.isInstanceOf(IllegalArgumentException.class)
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
	}

	@Nested
	class EqualsAndHashCodeTests {

		@Test
		void checkEqualsAndHashCode() throws NoSuchMethodException {
			// Arrange
			Method method = CorrectVariants.class.getDeclaredMethod("third");
			// Act
			PropertyProvider firstProvider = new PropertyProvider(method);
			PropertyProvider secondProvider = new PropertyProvider(method);
			// Assert
			assertThat(firstProvider).isEqualTo(secondProvider);
			assertThat(firstProvider.hashCode()).isEqualTo(secondProvider.hashCode());
		}

		@Test
		void checkNotEqualPropertyProvider() throws NoSuchMethodException {
			// Arrange
			Method firstMethod = CorrectVariants.class.getDeclaredMethod("first");
			Method secondMethod = CorrectVariants.class.getDeclaredMethod("second");
			// Act
			PropertyProvider firstProvider = new PropertyProvider(firstMethod);
			PropertyProvider secondProvider = new PropertyProvider(secondMethod);
			// Assert
			assertThat(firstProvider).isNotEqualTo(secondProvider);
			assertThat(firstProvider.hashCode()).isNotEqualTo(secondProvider.hashCode());
		}
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