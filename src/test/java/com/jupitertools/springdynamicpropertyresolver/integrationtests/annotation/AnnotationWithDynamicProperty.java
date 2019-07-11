package com.jupitertools.springdynamicpropertyresolver.integrationtests.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jupitertools.springdynamicpropertyresolver.IncludeDynamicProperty;


/**
 * @author Korovin Anatoliy
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@IncludeDynamicProperty(DynamicPropertyHolder.class)
public @interface AnnotationWithDynamicProperty {

}
