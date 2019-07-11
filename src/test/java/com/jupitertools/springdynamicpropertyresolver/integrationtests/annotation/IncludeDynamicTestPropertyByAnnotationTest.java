package com.jupitertools.springdynamicpropertyresolver.integrationtests.annotation;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link com.jupitertools.springdynamicpropertyresolver.IncludeDynamicProperty}
 *
 * @author Korovin Anatoliy
 */
@AnnotationWithDynamicProperty
@SpringBootTest
class IncludeDynamicTestPropertyByAnnotationTest {

    @Value("${key}")
    private String key;

    @Test
    void checkPropertyValueInjectInAnnotation() {
        assertThat(key).isEqualTo("051187");
    }
}
