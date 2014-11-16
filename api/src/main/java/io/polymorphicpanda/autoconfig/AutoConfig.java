package io.polymorphicpanda.autoconfig;

import javax.tools.StandardLocation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate interface to mark it as a target for source generation.
 *
 * @author Ranie Jade Ramiso
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoConfig {
    /**
     * Package where <code>filename</code> resides.
     */
    String packageName() default "";

    /**
     * Name of the properties file used to generated the config class.
     */
    String filename();

    /**
     * Location where to find the properties file.
     */
    StandardLocation resourceDir();
}
