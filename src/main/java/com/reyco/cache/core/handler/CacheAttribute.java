package com.reyco.cache.core.handler;

/** 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1 
 */
public interface CacheAttribute {
	
	Object getAttribute(String key);
	
	Object setAttribute(String key,Object value);
	
}
