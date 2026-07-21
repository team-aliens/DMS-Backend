package team.aliens.dms.scheduler.quartz.config

import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.SimpleScheduleBuilder
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.aliens.dms.scheduler.quartz.job.DaybreakStudyApplicationJob
import team.aliens.dms.scheduler.quartz.job.ExcludedStudentJob
import team.aliens.dms.scheduler.quartz.job.MealJob
import team.aliens.dms.scheduler.quartz.job.NotificationJob
import team.aliens.dms.scheduler.quartz.job.OutboxJob
import team.aliens.dms.scheduler.quartz.job.StudentTagJob

@Configuration
class QuartzConfig {

    @Bean
    fun outboxJobDetail(): JobDetail {
        return JobBuilder.newJob(OutboxJob::class.java)
            .withIdentity("outboxJob", "outbox")
            .withDescription("Process pending outbox messages")
            .storeDurably()
            .build()
    }

    @Bean
    fun outboxJobTrigger(outboxJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(outboxJobDetail)
            .withIdentity("outboxJobTrigger", "outbox")
            .withDescription("Trigger every 5 seconds")
            .withSchedule(
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever()
            )
            .build()
    }

    @Bean
    fun excludedStudentJobDetail(): JobDetail {
        return JobBuilder.newJob(ExcludedStudentJob::class.java)
            .withIdentity("excludedStudentJob", "student")
            .withDescription("Delete excluded students on last Monday of month")
            .storeDurably()
            .build()
    }

    @Bean
    fun excludedStudentJobTrigger(excludedStudentJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(excludedStudentJobDetail)
            .withIdentity("excludedStudentJobTrigger", "student")
            .withDescription("Every Monday at midnight (Asia/Seoul)")
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0 0 0 ? * MON")
                    .inTimeZone(java.util.TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }

    @Bean
    fun studentTagJobDetail(): JobDetail {
        return JobBuilder.newJob(StudentTagJob::class.java)
            .withIdentity("studentTagJob", "student")
            .withDescription("Update student tags daily")
            .storeDurably()
            .build()
    }

    @Bean
    fun studentTagJobTrigger(studentTagJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(studentTagJobDetail)
            .withIdentity("studentTagJobTrigger", "student")
            .withDescription("Every day at midnight (Asia/Seoul)")
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0 0 0 * * ?")
                    .inTimeZone(java.util.TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }

    @Bean
    fun mealJobDetail(): JobDetail {
        return JobBuilder.newJob(MealJob::class.java)
            .withIdentity("mealJob", "meal")
            .withDescription("Save all meals monthly")
            .storeDurably()
            .build()
    }

    @Bean
    fun mealJobTrigger(mealJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(mealJobDetail)
            .withIdentity("mealJobTrigger", "meal")
            .withDescription("Every 28th day of month at midnight (Asia/Seoul)")
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0 0 0 28 * ?")
                    .inTimeZone(java.util.TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }

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

    @Bean
    fun closeDaybreakStudyApplicationJobDetail(): JobDetail {
        return JobBuilder.newJob(DaybreakStudyApplicationJob::class.java)
            .withIdentity("expireDaybreakStudyApplicationJob", "daybreak")
            .withDescription("Expire daybreak study application")
            .storeDurably()
            .build()
    }

    @Bean
    fun closeDaybreakStudyApplicationJobTrigger(closeDaybreakStudyApplicationJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(closeDaybreakStudyApplicationJobDetail)
            .withIdentity("expireDaybreakStudyApplicationJobTrigger", "daybreak")
            .withDescription("Every Saturday at midnight (Asia/Seoul)")
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0 0 0 ? * SAT")
                    .inTimeZone(java.util.TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }
}
