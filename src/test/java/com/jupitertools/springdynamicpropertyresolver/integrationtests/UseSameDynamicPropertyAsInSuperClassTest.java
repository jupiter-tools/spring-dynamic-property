package com.jupitertools.springdynamicpropertyresolver.integrationtests;


import com.jupitertools.springdynamicpropertyresolver.DynamicTestProperty;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UseSameDynamicPropertyAsInSuperClassTest extends ParentTest {

	@Value("${first}")
	private String first;

	@Value("${second}")
	private String second;

	@DynamicTestProperty
	private static TestPropertyValues properties(){
		return TestPropertyValues.of("second=LOCAL");
	}

	@Test
	void dynamicPropertyFromSuperClass() {
		assertThat(first).isEqualTo("001");
		assertThat(second).isEqualTo("LOCAL");
	}
}
