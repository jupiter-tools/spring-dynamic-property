package com.jupitertools.springdynamicpropertyresolver.integrationtests.extension;

import com.jupitertools.springdynamicpropertyresolver.DynamicTestProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = InitVariableInExtensionStartedAfterSpringExtensionTest.class)
@ExtendWith(InitVariableInExtensionStartedAfterSpringExtensionTest.InitStaticVariableInExtension.class)
class InitVariableInExtensionStartedAfterSpringExtensionTest {

	@DynamicTestProperty
	private static TestPropertyValues props() {
		return TestPropertyValues
				.of("variable=" + InitStaticVariableInExtension.variable);
	}

	@Value("${variable}")
	private String variable;

	@Test
	void testVariableValueWhichInitializationInExtensionAfterSpringExtension() {
		assertThat(variable).isEqualTo("123");
	}

	/**
	 * That Extension started after the {@link SpringExtension} and
	 * the static variable in extension change in the {@link BeforeAllCallback}
	 */
	public static class InitStaticVariableInExtension
			implements Extension, BeforeAllCallback {

		static int variable = 0;

		@Override
		public void beforeAll(ExtensionContext context) throws Exception {
			variable = 123;
		}

	}

}
