package com.reyco.cache.core.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

/** 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1 
 */
public abstract class AbstractCacheAttributeSourcePointcut implements Pointcut,MethodMatcher{
	
	private ClassFilter classFilter = ClassFilter.TRUE;
	
	@Override
	public final boolean isRuntime() {
		return false;
	}
	
	@Override
	public ClassFilter getClassFilter() {
		return classFilter;
	}
	public void setClassFilter(ClassFilter classFilter) {
		this.classFilter = classFilter;
	}
	@Override
	public MethodMatcher getMethodMatcher() {
		return this;
	}
	@Override
	public final boolean matches(Method method, Class<?> targetClass, Object... args) {
		CacheAttributeSource cacheAttributeSource = getCacheAttributeSource();
		CacheAttribute cacheAttribute = cacheAttributeSource.getCacheAttribute(method, targetClass);
		for (Class<? extends Annotation> annotation : ReycoCacheAttributeSource.CACHE_ANNOTATION) {
			if(cacheAttribute.getAttribute(annotation.getName())!=null) {
				return true;
			}
		}
		return false;
	}
	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return matches(method, targetClass, false);
	}
	/** 
	 * @author  reyco
	 * @date    2022年6月14日
	 * @version v1.0.1 
	 * @return 
	 */
	protected abstract CacheAttributeSource getCacheAttributeSource();
}
