package com.reyco.cache.core.cache;

import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 *	通过静态内部类实现単例模式 
 * 		这种方法：线程安全，调用效率高，并且实现了延时加载
 * @author reyco
 *
 */
public class ConcurrentHashMapCache implements Cache {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 缓存对象 key value
	 */
	private final static ConcurrentMap<String, Object> store = new ConcurrentHashMap<>();
	/**
	 * 缓存的信息: 过去时间。。。
	 */
	private final static ConcurrentMap<String, ExpireInfo> storeObj = new ConcurrentHashMap<>();
	/**
	 * 私有构造器
	 */
	private ConcurrentHashMapCache() {}
	/**
	 * 私有获取属性的静态内部类
	 * @author reyco
	 *
	 */
	private static class Instance {
		private static final ConcurrentHashMapCache instance = new ConcurrentHashMapCache();
	}
	
	/**
	 * 方法没有同步，效率高！
	 * 
	 * @return
	 */
	public static ConcurrentHashMapCache getInstance() {
		return Instance.instance;
	}
	/**
	 * 添加元素
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public Boolean put(String key, Object value) {
		return put(key, value, null);
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
	@Override
	public Boolean put(String key, Object obj, Long duration) {
		if (StringUtils.isEmpty(key)|| null == obj) {
			return false;
		}
		Long ExpireTime = 0L;
		// 默认缓存不失效时长
		long currentTimeMillis = System.currentTimeMillis();
		if (null == duration || duration == -1) {
			// 默认缓存不失效
			ExpireTime = Long.MAX_VALUE;
		}else {
			ExpireTime = currentTimeMillis + 1000*duration;
		}
		// 构建缓存描述对象
		ExpireInfo expireInfo = new ExpireInfo();
		expireInfo.setStartTime(new Date());
		expireInfo.setDuration(duration);
		expireInfo.setExpireTime(ExpireTime);
		//添加缓存
		store.put(key, obj);
		storeObj.put(key, expireInfo);
		logger.debug("key="+key+",value="+obj+",expireTime="+ExpireTime+",添加到缓存中.");
		return true;
	}
	/**
	 * 添加元素
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public Boolean existsPut(String key, Object value) {
		return existsPut(key, value, null);
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
	@Override
	public Boolean existsPut(String key, Object obj, Long duration) {
		if (StringUtils.isEmpty(key) || null == obj) {
			return false;
		}
		if(store.containsKey(key)) {
			return false;
		}
		Long ExpireTime = 0L;
		// 默认缓存不失效时长
		long currentTimeMillis = System.currentTimeMillis();
		if (null == duration || duration == -1) {
			// 默认缓存不失效
			ExpireTime = Long.MAX_VALUE;
		}else {
			ExpireTime = currentTimeMillis + 1000*duration;
		}
		// 构建缓存描述对象
		ExpireInfo expireInfo = new ExpireInfo();
		expireInfo.setStartTime(new Date());
		expireInfo.setDuration(duration);
		expireInfo.setExpireTime(ExpireTime);
		//添加缓存
		store.put(key, obj);
		storeObj.put(key, expireInfo);
		logger.debug("key="+key+",value="+obj+",expireTime="+ExpireTime+",添加到缓存中.");
		return true;
	}
	
	
	/**
	 * 获取元素
	 * @param key
	 * @return
	 */
	@Override
	public Object get(String key) {
		if (StringUtils.isEmpty(key)) {
			throw new RuntimeException("key Can not be null");
		}
		// 判断key是否存在
		if (!storeObj.containsKey(key)) {
			return null;
		}
		// 是否过期
		ExpireInfo expireInfo = storeObj.get(key);
		long nowTime = System.currentTimeMillis();
		Long expireTime = expireInfo.getExpireTime();
		if (nowTime > expireTime) {
			// 过期
			remove(key);
			return null;
		}
		Object object = store.get(key);
		logger.debug("获取缓存，key="+key+",value="+object);
		return object;
	}
	
	/**
	 * 获取元素数量
	 * @param key
	 * @return
	 */
	@Override
	public Integer getSize() {
		return store.size();
	}

	/**
	 * 移除元素
	 * @param key
	 * @return
	 */
	@Override
	public Boolean remove(String key) {
		if (StringUtils.isEmpty(key)) {
			return false;
		}
		Object value = null;
		if(store.containsKey(key)) {
			value = store.get(key);
			store.remove(key);
			storeObj.remove(key);
			logger.debug("key="+key+",value="+value+",已过期,被移除了.");
		}
		return true;
	}
	/**
	 * 清空元素
	 * @return
	 */
	@Override
	public Boolean clear() {
		store.clear();
		storeObj.clear();
		logger.debug("缓存被清空.");
		return true;
	}
	/**
	 * 缓存删除策略
	 */
	@Override
	public void removeStrategy() {
		for (Entry<String, ExpireInfo> entry : storeObj.entrySet()) {
			ExpireInfo expireInfo = entry.getValue();
			// 是否过期
			long nowTime = System.currentTimeMillis();
			Long expireTime = expireInfo.getExpireTime();
			if (nowTime > expireTime) {
				String key = entry.getKey();
				// 过期
				remove(key);
			}
		}
	}
	@Override
	public Boolean exists(String key) {
		return store.containsKey(key);
	}
	@Override
	public Boolean expire(String key) {
		if(!store.containsKey(key)) {
			return true;
		}
		// 是否过期
		ExpireInfo expireInfo = storeObj.get(key);
		long nowTime = System.currentTimeMillis();
		Long expireTime = expireInfo.getExpireTime();
		if (nowTime > expireTime) {
			return true;
		}
		return false;
	}
}
