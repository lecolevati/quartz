package controller.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import controller.job.QuartzJob;

public class QuartzListener implements ServletContextListener {
	Scheduler scheduler = null;

	public void contextInitialized(ServletContextEvent servletContext) {
		System.out.println("Context Initialized");

		try {
			JobDetail job = JobBuilder.newJob(QuartzJob.class).withIdentity("dummyJobName", "group1").build();

			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("dummyTriggerName", "group1")
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
					.build();

			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent servletContext) {
		System.out.println("Context Destroyed");
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
