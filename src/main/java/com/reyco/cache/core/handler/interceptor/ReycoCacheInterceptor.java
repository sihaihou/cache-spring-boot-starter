package com.reyco.cache.core.handler.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.reyco.cache.core.handler.CacheAttribute;
import com.reyco.cache.core.handler.CacheAttributeSource;
import com.reyco.cache.core.handler.annotation.ReycoCacheEvict;
import com.reyco.cache.core.handler.annotation.ReycoCacheable;

/**
 * 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1
 */
public class ReycoCacheInterceptor extends ReycoCacheAspectSupport implements MethodInterceptor {

	private CacheAttributeSource cacheAttributeSource;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		CacheAttributeSource tas = getCacheAttributeSource();
		CacheAttribute cacheAttr = tas.getCacheAttribute(invocation.getMethod(), invocation.getThis().getClass());
		try {
			if (cacheAttr.getAttribute(ReycoCacheable.class.getName()) != null || cacheAttr.getAttribute(ReycoCacheEvict.class.getName()) != null) {
				return invokeInvocation(invocation);
			}
			return invocation.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			return invocation.proceed();
		}
	}
	public void setCacheAttributeSource(CacheAttributeSource cacheAttributeSource) {
		this.cacheAttributeSource = cacheAttributeSource;
	}
	public CacheAttributeSource getCacheAttributeSource() {
		return cacheAttributeSource;
	}
	
}
