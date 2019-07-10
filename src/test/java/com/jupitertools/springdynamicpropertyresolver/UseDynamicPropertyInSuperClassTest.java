package com.jupitertools.springdynamicpropertyresolver;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
