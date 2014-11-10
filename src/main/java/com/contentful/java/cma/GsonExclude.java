package com.contentful.java.cma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * GsonExclude.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface GsonExclude { }
