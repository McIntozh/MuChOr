/*
 * Copyright 2018 Dennis Schwarz <McIntozh@gmx.net>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.websplatter.muchor.scheduler.quartz;

import de.websplatter.muchor.MuChOr.Config;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import org.jboss.weld.context.bound.BoundRequestContext;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Dennis Schwarz <McIntozh@gmx.net>
 */
@ApplicationScoped
public class QuartzScheduler {

  private static final String QUARTZ_CRONT_PATTERN = "%s %s %s %s %s %s %s";

  private Scheduler scheduler;

  @Inject
  private Config config;

  public void startup(@Observes @Initialized(ApplicationScoped.class) Object init) {
    try {
      scheduler = StdSchedulerFactory.getDefaultScheduler();
      scheduler.start();
      schedulingJobs();
    } catch (ClassNotFoundException | SchedulerException ex) {
      throw new RuntimeException(ex);
    }
  }

  public void shutdown(@Observes @Destroyed(ApplicationScoped.class) Object deinit) {
    try {
      scheduler.shutdown();
    } catch (SchedulerException ex) {
      throw new RuntimeException(ex);
    }
  }

  private void schedulingJobs() throws ClassNotFoundException {
    Map<String, Map<String, Object>> jobsById = (Map<String, Map<String, Object>>) config.get("jobs");

    for (Entry<String, Map<String, Object>> entry : jobsById.entrySet()) {
      Map<String, Object> jobInfo = entry.getValue();
      Class<? extends de.websplatter.muchor.Job> jobClass = findJobClass(jobInfo);

      if (!jobClass.isAnnotationPresent(RequestScoped.class)) {
        Logger.getLogger(QuartzScheduler.class.getName()).log(Level.WARNING, "''{0}'' will not be scheduled because it is missing @RequestScoped annotation", new Object[]{jobClass});
        continue;
      }

      JobDataMap params = new JobDataMap((Map<String, Object>) jobInfo.getOrDefault("params", new HashMap<>()));
      params.put("_class", jobClass);

      JobDetail job = JobBuilder.newJob(JobWrapper.class)
          .withIdentity(entry.getKey())
          .usingJobData(params)
          .build();

      Trigger trigger;
      Object scheduleConfig = jobInfo.get("schedule");
      if (scheduleConfig instanceof Map) {
        scheduleConfig = convertScheduleToCron((Map<String, Object>) scheduleConfig);
      }

      Logger.getLogger(QuartzScheduler.class.getName()).log(Level.INFO, "Scheduling ''{0}'' with {1}", new Object[]{jobClass, scheduleConfig.toString()});

      trigger = newTrigger()
          .withIdentity(entry.getKey())
          .withSchedule(cronSchedule(scheduleConfig.toString())
              .withMisfireHandlingInstructionDoNothing())
          .build();

      try {
        scheduler.scheduleJob(job, trigger);
        if ("true".equalsIgnoreCase((String) jobInfo.get("runAtStartup"))) {
          scheduler.triggerJob(trigger.getJobKey());
        }
      } catch (SchedulerException sex) {
        Logger.getLogger(QuartzScheduler.class.getName()).log(Level.WARNING, sex.getMessage());
      }

    }

  }

  private Class<? extends de.websplatter.muchor.Job> findJobClass(Map<String, Object> jobInfo) throws ClassNotFoundException {

    try {
      String absoluteCLassName = "de.websplatter.muchor.channel." + jobInfo.get("class");
      Class c = Class.forName(absoluteCLassName);
      return c;
    } catch (ClassNotFoundException e) {
      Class c = Class.forName(jobInfo.get("class").toString());
      return c;
    }

  }

  public static class JobWrapper<K> implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      Map<String, Object> requestDataStore = new HashMap<>();
      BoundRequestContext boundRequestContext = CDI.current().select(BoundRequestContext.class).get();
      try {
        boundRequestContext.associate(requestDataStore);
        boundRequestContext.activate();

        Class<? extends de.websplatter.muchor.Job> clazz = (Class<? extends de.websplatter.muchor.Job>) context.getMergedJobDataMap().get("_class");
        try {
          de.websplatter.muchor.Job job = CDI.current().select(clazz).get();
          job.setParameters(context.getMergedJobDataMap().getWrappedMap());
          job.run();
        } catch (Throwable t) {
          Logger.getLogger(JobWrapper.class.getName()).log(Level.WARNING, "Job ''" + clazz.getName() + "'' is not supposed to throw an exception it did: " + t.getClass().getSimpleName(), t);
        }
      } finally {
        try {
          boundRequestContext.invalidate();
          boundRequestContext.deactivate();
        } finally {
          boundRequestContext.dissociate(requestDataStore);
        }
      }
    }
  }

  public String convertScheduleToCron(Map<String, Object> scheduleConfig) {
    return String.format(QUARTZ_CRONT_PATTERN,
        scheduleConfig.getOrDefault("second", "0"),
        scheduleConfig.getOrDefault("minute", "0"),
        scheduleConfig.getOrDefault("hour", "0"),
        scheduleConfig.getOrDefault("dayOfMonth", "*"),
        scheduleConfig.getOrDefault("month", "*"),
        scheduleConfig.getOrDefault("dayOfWeek", "?"),
        scheduleConfig.getOrDefault("year", "*")
    );
  }

}
