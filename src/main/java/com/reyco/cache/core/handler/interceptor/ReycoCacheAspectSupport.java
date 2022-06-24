package com.reyco.cache.core.handler.interceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.StringUtils;

import com.reyco.cache.core.cache.CacheUtils;
import com.reyco.cache.core.handler.SimpleKeyGenerator;
import com.reyco.cache.core.handler.annotation.ReycoCacheEvict;
import com.reyco.cache.core.handler.annotation.ReycoCacheable;

/**
 * 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1
 */
public class ReycoCacheAspectSupport implements BeanFactoryAware{
	private BeanFactory beanFactory;
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory=beanFactory;
	}
	/**
	 * 生成key
	 * @author  reyco
	 * @date    2022年6月24日
	 * @version v1.0.1 
	 * @param target
	 * @param method
	 * @param params
	 * @return
	 */
	protected String getKey(Object target, Method method,Object... params) {
		KeyGenerator keyGenerator = null;
		String keyGeneratorStr;
		ReycoCacheable reycoCacheable = method.getAnnotation(ReycoCacheable.class);
		if(reycoCacheable==null) {
			ReycoCacheEvict reycoCacheEvict = method.getAnnotation(ReycoCacheEvict.class);
			if(reycoCacheEvict!=null) {
				if(!StringUtils.isEmpty(keyGeneratorStr=reycoCacheEvict.keyGenerator())) {
					keyGenerator = getKeyGenerator(keyGeneratorStr);
				}
			}
		}else {
			if(!StringUtils.isEmpty(keyGeneratorStr=reycoCacheable.keyGenerator())) {
				keyGenerator = getKeyGenerator(keyGeneratorStr);
			}
			if(keyGenerator==null) {
				ReycoCacheEvict reycoCacheEvict = method.getAnnotation(ReycoCacheEvict.class);
				if(reycoCacheEvict!=null) {
					if(!StringUtils.isEmpty(keyGeneratorStr=reycoCacheEvict.keyGenerator())) {
						keyGenerator = getKeyGenerator(keyGeneratorStr);
					}
				}
			}
		}
		if(keyGenerator==null) {
			SimpleKeyGenerator simpleKeyGenerator = new SimpleKeyGenerator();
			return simpleKeyGenerator.generate(target, method, params).toString();
		}
		return keyGenerator.generate(target, method, params).toString();
	}
	
	private KeyGenerator getKeyGenerator(String KeyGeneratorName){
		return beanFactory.getBean(KeyGeneratorName,KeyGenerator.class);
	}
	public Object invokeInvocation(MethodInvocation invocation) {
		Method targetMethod = invocation.getMethod();
		Object[] parameterValues = invocation.getArguments();
		LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		String[] parameterNames = localVariableTableParameterNameDiscoverer.getParameterNames(targetMethod);
		Class<?>[] parameterTypes = targetMethod.getParameterTypes();
		ReycoCacheable reycoCacheable = targetMethod.getAnnotation(ReycoCacheable.class);
		// 2.1有注解
		String key = reycoCacheable.key();
		if(StringUtils.isEmpty(key)) {
			key = getKey(invocation.getThis(), targetMethod, parameterValues);
			return putOrGetCache(invocation, key, reycoCacheable.expireTime());
		}else {
			// 3.2key是否包含#字符
			if (!key.contains("#")) {
				throw new RuntimeException(reycoCacheable.keyGenerator()+",必须包含'#'特色符.");
			}
			// 3.3 获取KeyGenerator的名称
			key = key.substring(1);
			if(!key.contains(".")) {
				for (int i = 0; i < parameterNames.length; i++) {
					if (key.equals(parameterNames[i])) {
						key = key+"::"+parameterValues[i].toString();
						return putOrGetCache(invocation, key, reycoCacheable.expireTime());
					}
				}
				throw new RuntimeException(reycoCacheable.keyGenerator()+",出错");
			}
			String[] keyArray = key.split("\\.");
			// 3.4 获取KeyGenerator值
			for (int i = 0; i < parameterNames.length; i++) {
				if ((key=keyArray[0]).equals(parameterNames[i])) {
					// 如果注解的keyGenerator和参数的名称一直，取对应参数的值作为keyGenerator
					Field[] fields = parameterTypes[i].getDeclaredFields();
					for (int j=0;j<fields.length;j++) {
						Field field = fields[j];
						field.setAccessible(true);
						String fieldName = field.getName();
						if((key=keyArray[1]).equals(fieldName)) {
							try {
								Object fieldValue = fields[j].get(parameterValues[j]);
								key = keyArray[0]+"."+keyArray[1];
								return invoke(invocation, keyArray, 2, key,fieldValue,reycoCacheable.expireTime());
							} catch (IllegalArgumentException | IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			throw new RuntimeException(reycoCacheable.keyGenerator()+",出错");
		}
	}
	
	protected Object invoke(MethodInvocation invocation,String[] keyArray,int current,String key,Object value,Long expireTime){
		if(keyArray.length<=current) {
			key = key+"::"+value.toString();
			return putOrGetCache(invocation,key,expireTime);
		}
		String currentKey = keyArray[current];
		if(StringUtils.isEmpty(currentKey)) {
			throw new RuntimeException("Key configuration error");
		}
		Field[] fields = value.getClass().getDeclaredFields();
		for (int i=0;i<fields.length;i++) {
			Field field = fields[i];
			field.setAccessible(true);
			String fieldName = field.getName();
			if(currentKey.equals(fieldName)) {
				try {
					return invoke(invocation,keyArray,current+1,key+"."+fieldName,field.get(value),expireTime);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		throw new RuntimeException("Key configuration error");
	}
	
	protected Object putOrGetCache(MethodInvocation invocation,String key,long expireTime) {
		key = getKey(invocation.getThis(), invocation.getMethod(),key);
		Object value;
		if ((value=CacheUtils.get(key))==null) {
			// 缓存中不存在当前缓存,执行目标方法
			try {
				value = invocation.proceed();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			if (expireTime < 1) {
				CacheUtils.put(key, value);
			} else {
				CacheUtils.put(key, value, expireTime);
			}
		}
		return value;
	}
}
