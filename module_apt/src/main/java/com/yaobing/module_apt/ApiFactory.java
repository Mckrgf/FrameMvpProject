package com.yaobing.module_apt;

/**
 * Created by baixiaokang on 16/12/28.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ApiFactory {
    String DEFAULT = "HttpClient";
    String name() default DEFAULT;

}
