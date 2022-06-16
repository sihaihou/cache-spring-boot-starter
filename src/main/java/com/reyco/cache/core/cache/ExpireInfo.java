package com.reyco.cache.core.cache;

import java.util.Date;

/**
 * 描述缓存对象
 * @author reyco
 *
 */
public class ExpireInfo {
	
	/**
	 * 开始时间
	 */
	private Date startTime;
	/**
	 * 存活时长
	 */
	private Long duration;
	/**
	 * 结束时间毫秒数
	 */
	private Long ExpireTime;

	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Long getExpireTime() {
		return ExpireTime;
	}
	public void setExpireTime(Long expireTime) {
		ExpireTime = expireTime;
	}
}
