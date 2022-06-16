package com.reyco.cache.core.handler.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import com.reyco.cache.core.handler.config.ReycoCacheConfigurationSelector;

/** 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1 
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ReycoCacheConfigurationSelector.class)
public @interface EnableReycoCache {
	
	boolean proxyTargetClass() default false;

	AdviceMode mode() default AdviceMode.PROXY;
	
	int order() default Ordered.LOWEST_PRECEDENCE;
	
}
