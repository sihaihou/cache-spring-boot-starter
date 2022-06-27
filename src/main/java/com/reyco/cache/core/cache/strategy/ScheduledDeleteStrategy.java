package com.reyco.cache.core.cache.strategy;

/**
 * 简单的删除策略--定时扫描删除
 * @author reyco
 * @date 2022.06.27
 * @version v1.0.1
 */
public interface ScheduledDeleteStrategy extends DeleteStrategy {

	void scheduledDeleteCache();

}
