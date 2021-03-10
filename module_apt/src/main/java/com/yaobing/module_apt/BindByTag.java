package com.yaobing.module_apt;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by wangshizhan on 2017/12/8.
 * Email:wangshizhan@supcon.com
 */

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindByTag {
    /** View ID to which the field will be bound. */
    String value();
}