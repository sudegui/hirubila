package com.m4f.utils.cache.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
	public String cacheName() default "default";
}
