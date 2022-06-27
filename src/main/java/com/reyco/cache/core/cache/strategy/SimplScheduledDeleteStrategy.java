package com.reyco.cache.core.cache.strategy;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

import com.reyco.cache.core.cache.ConcurrentHashMapCache;

/**
 * 	定时任务删除过期的缓存对象
 * @author housihai
 *
 */
@Component
public class SimplScheduledDeleteStrategy implements ScheduledDeleteStrategy,Job{
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		scheduledDeleteCache();
	}
	@PostConstruct
	public void task() {
		try {
			Date startTime = new Date();
			startTime.setTime(startTime.getTime()+1000*30);
			// 创建一个JobDetail
			JobDetail taskJobDetail = JobBuilder.newJob(SimplScheduledDeleteStrategy.class)
					.withIdentity("TaskDetailJob","TaskDetailGroup")
					.build();
			// Cron表达式：  秒 分 时 日 月 周 年
			CronTrigger taskTrigger = (CronTrigger)TriggerBuilder.newTrigger()
							// 30秒后执行任务
							.startAt(startTime)
							// 每分钟执行
							.withSchedule(CronScheduleBuilder.cronSchedule("0,1 * * * * ? "))
							.build();
			// 创建SchedulerFactory
			SchedulerFactory sf = new StdSchedulerFactory();
			// 创建Scheduler
			Scheduler scheduler = sf.getScheduler();
			scheduler.start();
			scheduler.scheduleJob(taskJobDetail,taskTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteCache() {
		scheduledDeleteCache();
	}

	@Override
	public void scheduledDeleteCache() {
		ConcurrentHashMapCache instance = ConcurrentHashMapCache.getInstance();
		instance.removeStrategy();
	}
}
