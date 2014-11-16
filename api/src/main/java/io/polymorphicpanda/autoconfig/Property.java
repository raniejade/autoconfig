package io.polymorphicpanda.autoconfig;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate method to mark it as a target for source generation.
 *
 * @author Ranie Jade Ramiso
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Property {
    /**
     * The key used to fetch the return value of the generated method.
     * @return property key.
     */
    String key();
}
