package com.jupitertools.springdynamicpropertyresolver.integrationtests.inheritance;


import com.jupitertools.springdynamicpropertyresolver.DynamicTestProperty;

import org.springframework.boot.test.util.TestPropertyValues;

/**
 * @author Korovin Anatoliy
 */
public abstract class ParentTest {

	@DynamicTestProperty
	private static TestPropertyValues parentProperties(){
		return TestPropertyValues.of("first=001",
		                             "second=002");
	}
}
