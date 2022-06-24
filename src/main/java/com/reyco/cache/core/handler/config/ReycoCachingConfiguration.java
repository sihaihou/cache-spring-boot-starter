package com.reyco.cache.core.handler.config;

import org.springframework.aop.PointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.reyco.cache.core.handler.CacheAttributeSource;
import com.reyco.cache.core.handler.ReycoCacheAttributeSource;
import com.reyco.cache.core.handler.SimpleKeyGenerator;
import com.reyco.cache.core.handler.advisor.CachePointcutAdvisor;
import com.reyco.cache.core.handler.interceptor.ReycoCacheInterceptor;

/**
 * @author reyco
 * @date 2022.06.14
 * @version v1.0.1
 */
@Configuration
public class ReycoCachingConfiguration {
	
	@Bean
	public PointcutAdvisor cachePointcutAdvisor(ReycoCacheInterceptor interceptor,CacheAttributeSource testAttributeSource) {
        CachePointcutAdvisor advisor = new CachePointcutAdvisor();
        advisor.setCacheAttributeSource(testAttributeSource);
        advisor.setAdvice(interceptor);
        return advisor;
	}
	
	@Bean
	public CacheAttributeSource cacheAttributeSource() {
		return new ReycoCacheAttributeSource();
	}
	
	@Bean
	public ReycoCacheInterceptor cacheInterceptor(CacheAttributeSource cacheAttributeSource) {
		ReycoCacheInterceptor interceptor = new ReycoCacheInterceptor();
		interceptor.setCacheAttributeSource(cacheAttributeSource);
		return interceptor;
	}
	@Bean("defaultKeyGenerator")
	@ConditionalOnMissingBean(KeyGenerator.class)
	public KeyGenerator defaultKeyGenerator() {
		SimpleKeyGenerator simpleKeyGenerator = new SimpleKeyGenerator();
		return simpleKeyGenerator;
	}
}
