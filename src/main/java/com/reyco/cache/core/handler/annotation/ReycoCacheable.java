package com.reyco.cache.core.handler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 使用缓存
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1 
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReycoCacheable {
	/**
	 * 缓存名称
	 * @return
	 */
	String name() default "";
	/**
	 * 缓存的keyGenerator
	 * @return
	 */
	String keyGenerator() default "";
	/**
	 * 缓存的过期时间,默认30分钟
	 * @return
	 */
	long expireTime() default 60*30L;
}