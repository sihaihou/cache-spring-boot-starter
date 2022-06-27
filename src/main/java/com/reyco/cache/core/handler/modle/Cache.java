package com.reyco.cache.core.handler.modle;


/** 
 * @author  reyco
 * @date    2022.06.27
 * @version v1.0.1 
 */
public class Cache {
	private String key;
	private String value;
	private Long expire;
	/**
	 * 
	 */
	public Cache() {
		// TODO Auto-generated constructor stub
	}
	public Cache(String key, String value, Long expire) {
		super();
		this.key = key;
		this.value = value;
		this.expire = expire;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Long getExpire() {
		return expire;
	}
	public void setExpire(Long expire) {
		this.expire = expire;
	}
}
