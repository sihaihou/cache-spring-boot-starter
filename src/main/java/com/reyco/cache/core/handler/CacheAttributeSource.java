package com.reyco.cache.core.handler;

import java.lang.reflect.Method;

import org.springframework.lang.Nullable;

/** 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1 
 */
public interface CacheAttributeSource {
	
	CacheAttribute getCacheAttribute(Method method, @Nullable Class<?> targetClass);
	
}
