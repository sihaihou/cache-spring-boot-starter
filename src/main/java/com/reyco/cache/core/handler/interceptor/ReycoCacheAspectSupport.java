package com.reyco.cache.core.handler.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.StringUtils;

import com.reyco.cache.core.cache.CacheUtils;
import com.reyco.cache.core.handler.annotation.ReycoCacheEvict;
import com.reyco.cache.core.handler.annotation.ReycoCacheable;

/**
 * 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1
 */
public class ReycoCacheAspectSupport {
	/**
	 * 
	 * @author  reyco
	 * @date    2022年6月15日
	 * @version v1.0.1 
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	protected Object processReycoCacheEvict(MethodInvocation invocation) throws Throwable {
		Class<? extends Object> targetClass = invocation.getThis().getClass();
		Method method = invocation.getMethod();
		Object[] parameterValues = invocation.getArguments();
		LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
		ReycoCacheEvict reycoCacheEvict = method.getAnnotation(ReycoCacheEvict.class);
		String cacheName = getCacheName(reycoCacheEvict);
		KeyGenerator keyGenerator = getKeyGenerator(reycoCacheEvict, targetClass, method, parameterNames, parameterValues);
		String key = getKey(cacheName, keyGenerator);
		CacheUtils.remove(key);
		return invocation.proceed();
	}
	/**
	 * 
	 * @author  reyco
	 * @date    2022年6月15日
	 * @version v1.0.1 
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	protected Object processReycoCacheable(MethodInvocation invocation) throws Throwable {
		Class<? extends Object> targetClass = invocation.getThis().getClass();
		Method method = invocation.getMethod();
		Object[] parameterValues = invocation.getArguments();
		LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(method);
		ReycoCacheable reycoCacheable = method.getAnnotation(ReycoCacheable.class);
		String cacheName = getCacheName(reycoCacheable);
		KeyGenerator keyGenerator = getKeyGenerator(reycoCacheable, targetClass, method, parameterNames,parameterValues);
		// 3.1有注解
		String key = getKey(cacheName, keyGenerator);
		// 通过key获取缓存value
		Object object = CacheUtils.get(key);
		if (null == object) {
			// 缓存中不存在当前缓存,执行目标方法
			object = invocation.proceed();
			long expireTime = reycoCacheable.expireTime();
			if (expireTime < 1) {
				CacheUtils.put(key, object);
			} else {
				CacheUtils.put(key, object, expireTime);
			}
		}
		return object;
	}
	/**
	 * 获取key
	 * @param cacheName	                       缓存名称
	 * @param keyGeneratorObj   keyGeneratorObj对象
	 * @return
	 */
	protected String getKey(String cacheName,KeyGenerator keyGeneratorObj) {
		String key = "";
		// 缓存
		String keyGenerator = keyGeneratorObj.getKeyGenerator();
		key = cacheName + keyGenerator;
		return key;
	}
	/**
	 * 获取缓存名称
	 * @param reycoCacheable
	 * @return
	 */
	protected String getCacheName(ReycoCacheable reycoCacheable) {
		// 缓存名称
		String cacheName = "";
		String name = reycoCacheable.name();
		if(!StringUtils.isEmpty(name)) {
			cacheName = name+"::";
		}
		return cacheName;
	}
	/**
	 * 获取缓存名称
	 * @param reycoCacheEvict
	 * @return
	 */
	protected String getCacheName(ReycoCacheEvict reycoCacheEvict) {
		// 缓存名称
		String cacheName = "";
		String name = reycoCacheEvict.name();
		if(!StringUtils.isEmpty(name)) {
			cacheName = name+"::";
		}
		return cacheName;
	}
	/**
	 * 获取KeyGenerator
	 * @param reycoCacheable
	 * @param clazz
	 * @param method
	 * @param paramNames
	 * @param paramValues
	 * @return
	 */
	protected KeyGenerator getKeyGenerator(ReycoCacheable reycoCacheable,Class<?> clazz,Method method, String[] paramNames, Object[] paramValues) {
		// KeyGenerator
		KeyGenerator keyGeneratorObj = null;
		// 2.1有注解
		String keyGenerator = reycoCacheable.keyGenerator();
		if(StringUtils.isEmpty(keyGenerator)) {
			keyGenerator = createKeyGenerator(clazz,method,paramValues);
		}else {
			// 3.2key是否包含#字符
			if (keyGenerator.contains("#")) {
				// 3.3 获取KeyGenerator的名称
				String keyGeneratorName = keyGenerator.substring(1);
				// 3.4 获取KeyGenerator值
				for (Object paramValue : paramValues) {
					for (int j = 0; j < paramNames.length; j++) {
						if (keyGeneratorName.equals(paramNames[j])) {
							// 如果注解的keyGenerator和参数的名称一直，取对应参数的值作为keyGenerator
							keyGenerator = paramValues[j].toString();
						}
					}
				}
			}
		}
		keyGeneratorObj = new KeyGenerator();
		keyGeneratorObj.setKeyGenerator(keyGenerator);
		return keyGeneratorObj;
	}
	/**
	 * 获取KeyGenerator
	 * @param reycoCacheEvict
	 * @param clazz
	 * @param method
	 * @param paramNames
	 * @param paramValues
	 * @return
	 */
	protected KeyGenerator getKeyGenerator(ReycoCacheEvict reycoCacheEvict,Class<?> clazz,Method method, String[] paramNames, Object[] paramValues) {
		// KeyGenerator
		KeyGenerator keyGeneratorObj = null;
		// 2.1有注解
		String keyGenerator = reycoCacheEvict.keyGenerator();
		if(StringUtils.isEmpty(keyGenerator)) {
			keyGenerator = createKeyGenerator(clazz,method,paramValues);
		}else {
			// 3.2key是否包含#字符
			if (keyGenerator.contains("#")) {
				// 3.3 获取KeyGenerator的名称
				String keyGeneratorName = keyGenerator.substring(1);
				// 3.4 获取KeyGenerator值
				for (Object paramValue : paramValues) {
					for (int j = 0; j < paramNames.length; j++) {
						if (keyGeneratorName.equals(paramNames[j])) {
							// 如果注解的keyGenerator和参数的名称一直，取对应参数的值作为keyGenerator
							keyGenerator = paramValues[j].toString();
						}
					}
				}
			}
		}
		keyGeneratorObj = new KeyGenerator();
		keyGeneratorObj.setKeyGenerator(keyGenerator);
		return keyGeneratorObj;
	}
	/**
	 * key的默认生成器
	 * @param target	目标对象
	 * @param method	目标方法
	 * @param params	目标方法参数
	 * @return
	 */
	protected String createKeyGenerator(Object target, Method method, Object... params) {
		// 缓存的key
		StringBuilder sb = new StringBuilder();
		sb.append(target.getClass());
		sb.append(method.getName());
		for (Object obj : params) {
			sb.append(obj.toString());
		}
		return sb.toString();
	}	
	/**
	 * key生成器
	 * @author  reyco
	 * @date    2022.06.13
	 * @version v1.0.1
	 */
	protected static class KeyGenerator {
		String key;

		public String getKeyGenerator() {
			return key;
		}
		public void setKeyGenerator(String key) {
			this.key = key;
		}
	} 
}
