package com.jupitertools.springdynamicpropertyresolver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.util.Assert;

/**
 * Created on 2019-07-10
 * <p>
 * Wrapper under the method which annotated by @DynamicTestProperty,
 * supposed to validating this method and retrieving a value of the {@link TestPropertyValues}
 *
 * @author Korovin Anatoliy
 */
public class PropertyProvider {

	private final Method method;

	/**
	 * Create new {@link PropertyProvider} from method which annotated by the {@link DynamicTestProperty}
	 *
	 * @param method method from test class with supplier of {@link TestPropertyValues}
	 */
	public PropertyProvider(Method method) {

		Assert.notNull(method.getAnnotation(DynamicTestProperty.class),
		               "PropertyProvider must be annotated by @DynamicTestProperty");

		Assert.isTrue(Modifier.isStatic(method.getModifiers()),
		              "Annotation @DynamicTestProperty must be used on a static method.");

		Assert.isTrue(method.getReturnType().equals(TestPropertyValues.class),
		              "@DynamicTestProperty method must return the instance of TestPropertyValues.");

		this.method = method;
	}

	/**
	 * evaluate TestPropertyValues from {@link PropertyProvider} method
	 *
	 * @return evaluated TestPropertyValues
	 */
	public TestPropertyValues getTestPropertyValues() {
		try {
			this.method.setAccessible(true);
			return (TestPropertyValues) this.method.invoke(null);
		} catch (Exception ex) {
			throw new IllegalArgumentException(
					"Error while trying to get a value of dynamic properties.", ex);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PropertyProvider that = (PropertyProvider) o;
		return Objects.equals(method, that.method);
	}

	@Override
	public int hashCode() {
		return Objects.hash(method);
	}
}