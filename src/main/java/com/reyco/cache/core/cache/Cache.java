package com.reyco.cache.core.cache;

import java.util.List;
import java.util.Map;

/**
 * 
 * 缓存顶级接口
 * 
 * @author reyco
 *
 */
public interface Cache {
	/**
	 * 添加缓存对象
	 * @param key 		缓存的key,通过该key可以获取对应的缓存对象
	 * @param obj		缓存的对象...  
	 * @return
	 */
	public Boolean put(String key, Object obj);
	/**
	 * 添加缓存对象
	 * @param key 			缓存的key,通过该key可以获取对应的缓存对象
	 * @param obj			缓存的对象...  
	 * @param duration		存活时长...
	 * @return
	 */
	public Boolean put(String key, Object obj,Long duration);
	/**
	 * 添加缓存对象:有则不添加，没有则添加
	 * @param key
	 * @param obj
	 * @return
	 */
	public Boolean existsPut(String key, Object value);
	/**
	 * 添加缓存对象:有则不添加，没有则添加
	 * @param key
	 * @param obj
	 * @param duration
	 * @return
	 */
	public Boolean existsPut(String key, Object obj, Long duration);
	/**
	 * 获取缓存对象
	 * @param key	缓存的key,通过该key可以获取对应的缓存对象
	 * @return
	 */
	public Object get(String key);
	/**
	 * 获取所有缓存对象
	 * @author  reyco
	 * @date    2022年6月27日
	 * @version v1.0.1 
	 * @return
	 */
	public Map<String,Object> getAll();
	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	public Boolean exists(String key);
	/**
	 * 判断key是否过期
	 * @param key
	 * @return
	 */
	public Boolean expire(String key);
	/**
	 * 缓存的数量大小
	 * @return
	 */
	public Integer getSize();
	/**
	 * 移除缓存
	 * @param key   缓存的key,通过该key可以获取对应的缓存对象
	 * @return
	 */
	public Boolean remove(String key);
	/**
	 * 清空缓存
	 * @return
	 */
	public Boolean clear();
	/**
	 * 缓存删除策略
	 */
	public void removeStrategy();
}
