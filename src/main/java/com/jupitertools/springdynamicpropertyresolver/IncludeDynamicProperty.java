package com.jupitertools.springdynamicpropertyresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2019-07-11
 * <p>
 * You can use this annotation to declare a class for processing
 * {@link DynamicTestProperty} methods.
 * <p>
 * This is supposed to use {@link DynamicTestProperty} in your meta-annotations
 * by declaring classes with dynamic properties in the {@link IncludeDynamicProperty} annotation.
 *
 * @author Korovin Anatoliy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(IncludeDynamicProperties.class)
@Inherited
public @interface IncludeDynamicProperty {

    Class<?>[] value();
}
