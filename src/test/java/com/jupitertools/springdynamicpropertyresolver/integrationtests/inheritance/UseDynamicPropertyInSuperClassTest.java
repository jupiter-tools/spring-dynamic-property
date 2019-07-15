package com.jupitertools.springdynamicpropertyresolver.integrationtests.inheritance;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link com.jupitertools.springdynamicpropertyresolver.DynamicTestProperty}
 *
 * @author Korovin Anatoliy
 */
@SpringBootTest(classes = UseDynamicPropertyInSuperClassTest.class)
class UseDynamicPropertyInSuperClassTest extends ParentTest {

	@Value("${first}")
	private String first;

	@Value("${second}")
	private String second;

	@Test
	void dynamicPropertyFromSuperClass() {
		assertThat(first).isEqualTo("001");
		assertThat(second).isEqualTo("002");
	}
}
