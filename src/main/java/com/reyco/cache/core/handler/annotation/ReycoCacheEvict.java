package com.reyco.cache.core.handler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * 删除缓存
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1 
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReycoCacheEvict {
	/**
	 * cacheName
	 * @author  reyco
	 * @date    2022年6月24日
	 * @version v1.0.1 
	 * @return
	 */
	String value() default "";
	/**
	 * cacheName
	 * @return
	 */
	String cacheName() default "";
	/**
	 * 获取的key
	 * @author  reyco
	 * @date    2022年6月24日
	 * @version v1.0.1 
	 * @return
	 */
	String key()  default "";
	/**
	 * 缓存的key生成器
	 * @return
	 */
	String keyGenerator() default "";
}
