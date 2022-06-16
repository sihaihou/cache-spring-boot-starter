package com.reyco.cache.core.cache;

public class CacheUtils {

	private static ConcurrentHashMapCache getConcurrentHashMapCache() {
		return ConcurrentHashMapCache.getInstance();
	}
	/**
	 * 添加元素
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @param duration
	 *            有效时间/单位秒
	 * @return
	 */
	public static void put(String key,Object value,Long expireTime) {
		ConcurrentHashMapCache concurrentHashMapCache = getConcurrentHashMapCache();
		concurrentHashMapCache.put(key,value,expireTime);
	}
	/**
	 * 添加元素
	 * @param key
	 * @param value
	 * @return
	 */
	public static void put(String key,Object value) {
		ConcurrentHashMapCache concurrentHashMapCache = getConcurrentHashMapCache();
		concurrentHashMapCache.put(key, value);
	}
	/**
	 * 添加元素
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @param duration
	 *            有效时间/单位秒
	 * @return
	 */
	public static void existsPut(String key,Object value,Long expireTime) {
		ConcurrentHashMapCache concurrentHashMapCache = getConcurrentHashMapCache();
		concurrentHashMapCache.existsPut(key,value,expireTime);
	}
	/**
	 * 添加元素
	 * @param key
	 * @param value
	 * @return
	 */
	public static void existsPut(String key,Object value) {
		ConcurrentHashMapCache concurrentHashMapCache = getConcurrentHashMapCache();
		concurrentHashMapCache.existsPut(key, value);
	}
	/**
	 * 获取缓存对象
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		ConcurrentHashMapCache concurrentHashMapCache = getConcurrentHashMapCache();
		return concurrentHashMapCache.get(key);
	}
	/**
	 * 移除缓存对象
	 * @param key
	 * @return
	 */
	public static Object remove(String key) {
		ConcurrentHashMapCache concurrentHashMapCache = getConcurrentHashMapCache();
		return concurrentHashMapCache.remove(key);
	}
	/**
	 * 缓存是否存在   true存在  false不存在
	 * @param key
	 * @return
	 */
	public static Boolean exists(String key) {
		ConcurrentHashMapCache concurrentHashMapCache = getConcurrentHashMapCache();
		return concurrentHashMapCache.exists(key);
	}
	/**
	 * 缓存是否过期  true过期  false未过期
	 * @param key
	 * @return
	 */
	public static Object expire(String key) {
		ConcurrentHashMapCache concurrentHashMapCache = getConcurrentHashMapCache();
		return concurrentHashMapCache.expire(key);
	}
}
