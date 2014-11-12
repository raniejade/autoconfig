package com.raniejaderamiso.autoconfig;

import javax.tools.StandardLocation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Ranie Jade Ramiso
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoConfig {
    String packageName() default "";
    String filename();
    StandardLocation resourceDir();
}
