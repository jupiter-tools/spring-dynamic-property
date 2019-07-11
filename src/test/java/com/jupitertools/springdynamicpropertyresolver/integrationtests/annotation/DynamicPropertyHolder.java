package com.jupitertools.springdynamicpropertyresolver.integrationtests.annotation;

import com.jupitertools.springdynamicpropertyresolver.DynamicTestProperty;

import org.springframework.boot.test.util.TestPropertyValues;

/**
 * @author Korovin Anatoliy
 */
public class DynamicPropertyHolder {

    @DynamicTestProperty
    private static TestPropertyValues property(){
        return TestPropertyValues.of("key=051187");
    }
}
