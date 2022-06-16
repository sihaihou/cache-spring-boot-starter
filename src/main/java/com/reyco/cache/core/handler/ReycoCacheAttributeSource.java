package com.reyco.cache.core.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.reyco.cache.core.handler.annotation.ReycoCacheEvict;
import com.reyco.cache.core.handler.annotation.ReycoCacheable;

/** 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1 
 */
public class ReycoCacheAttributeSource implements CacheAttributeSource {
	
	public static final List<Class<? extends Annotation>> CACHE_ANNOTATION = new ArrayList<>(3);

	static {
		CACHE_ANNOTATION.add(ReycoCacheable.class);
		CACHE_ANNOTATION.add(ReycoCacheEvict.class);
	}
	@Override
	public CacheAttribute getCacheAttribute(Method method,Class<?> targetClass) {
		ReycoCacheAttribute cacheAttribute = new ReycoCacheAttribute();
		for (Class<? extends Annotation> annotationClass: CACHE_ANNOTATION) {
			if(method.isAnnotationPresent(annotationClass)) {
				cacheAttribute.setAttribute(annotationClass.getName(), annotationClass);
			}
		}
		return cacheAttribute;
	}

}
