package com.reyco.cache.core.handler.advisor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;

import com.reyco.cache.core.handler.AbstractCacheAttributeSourcePointcut;
import com.reyco.cache.core.handler.CacheAttributeSource;

/** 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1 
 */
public class CachePointcutAdvisor extends AbstractPointcutAdvisor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6071182711086134240L;

	private Advice advice;
	
	private CacheAttributeSource cacheAttributeSource;
	
	private final AbstractCacheAttributeSourcePointcut pointcut = new AbstractCacheAttributeSourcePointcut() {
		@Override
		protected CacheAttributeSource getCacheAttributeSource() {
			return cacheAttributeSource;
		}
	};
	public void setAdvice(Advice advice) {
		this.advice = advice;
	}
	@Override
	public Advice getAdvice() {
		return this.advice;
	}
	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}
	public void setCacheAttributeSource(CacheAttributeSource cacheAttributeSource) {
		this.cacheAttributeSource = cacheAttributeSource;
	}
}
