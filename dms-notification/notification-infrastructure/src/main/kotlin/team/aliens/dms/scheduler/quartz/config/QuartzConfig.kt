package team.aliens.dms.scheduler.quartz.config

import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.aliens.dms.scheduler.quartz.job.NotificationJob

@Configuration
class QuartzConfig {

    @Bean
    fun notificationJobDetail(): JobDetail {
        return JobBuilder.newJob(NotificationJob::class.java)
            .withIdentity("notificationJob", "notification")
            .withDescription("Delete old notifications daily")
            .storeDurably()
            .build()
    }

    @Bean
    fun notificationJobTrigger(notificationJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(notificationJobDetail)
            .withIdentity("notificationJobTrigger", "notification")
            .withDescription("Every day at midnight (Asia/Seoul)")
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0 0 0 * * ?")
                    .inTimeZone(java.util.TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }
}
