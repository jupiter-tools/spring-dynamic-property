package com.jupitertools.springdynamicpropertyresolver.integrationtests.simple;

import com.jupitertools.springdynamicpropertyresolver.DynamicTestProperty;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = SimpleTest.class)
class SimpleTest {

	@DynamicTestProperty
	private static TestPropertyValues props() {
		return TestPropertyValues
				.of("variable=" + (int) Math.sqrt(64));
	}

	@Value("${variable}")
	private int variable;

	@Test
	void testSqrt() {
		assertThat(variable).isEqualTo(8);
	}
}
