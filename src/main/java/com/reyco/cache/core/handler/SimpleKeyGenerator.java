package com.reyco.cache.core.handler;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.StringUtils;

import com.reyco.cache.core.handler.annotation.ReycoCacheEvict;
import com.reyco.cache.core.handler.annotation.ReycoCacheable;

/** 
 * @author  reyco
 * @date    2022.06.24
 * @version v1.0.1 
 */
public class SimpleKeyGenerator implements KeyGenerator{
	@Override
	public Object generate(Object target, Method method, Object... params) {
		ReycoCacheable reycoCacheable = method.getDeclaredAnnotation(ReycoCacheable.class);
		String cacheName;
		if(reycoCacheable==null) {
			ReycoCacheEvict reycoCacheEvict = method.getDeclaredAnnotation(ReycoCacheEvict.class);
			cacheName = reycoCacheEvict.value();
			if(StringUtils.isEmpty(cacheName)) {
				cacheName = reycoCacheEvict.cacheName();
			}
		}else {
			cacheName = reycoCacheable.value();
			if(StringUtils.isEmpty(cacheName)) {
				cacheName = reycoCacheable.cacheName();
			}
		}
		if(params==null) {
			if(StringUtils.isEmpty(cacheName)) {
				return target.getClass().getName()+'.'+method.getName()+"()";
			}
			return cacheName+"@"+target.getClass().getName()+'.'+method.getName()+"()";
		}
		if(StringUtils.isEmpty(cacheName)) {
			return target.getClass().getName()+'.'+method.getName()+"()."+params[0];
		}
		return cacheName+"@"+target.getClass().getName()+'.'+method.getName()+"()."+params[0];
	}
}