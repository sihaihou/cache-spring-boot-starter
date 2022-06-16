package com.reyco.cache.core.handler;

import java.util.HashMap;
import java.util.Map;

/** 
 * @author  reyco
 * @date    2022.06.14
 * @version v1.0.1 
 */
public class ReycoCacheAttribute implements CacheAttribute{
	
	private Map<String, Object> attributes = new HashMap<String,Object>();

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@Override
	public Object setAttribute(String key, Object value) {
		return attributes.put(key, value);
	}
	
}
