package team.aliens.dms.scheduler.quartz.config

import org.quartz.CronScheduleBuilder
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.SimpleScheduleBuilder
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.aliens.dms.scheduler.quartz.job.ExcludedStudentJob
import team.aliens.dms.scheduler.quartz.job.MealJob
import team.aliens.dms.scheduler.quartz.job.OutboxJob
import team.aliens.dms.scheduler.quartz.job.StudentTagJob
import team.aliens.dms.scheduler.quartz.job.StudyRoomJob
import team.aliens.dms.scheduler.quartz.job.VolunteerScoreJob

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
    fun studyRoomJobDetail(): JobDetail {
        return JobBuilder.newJob(StudyRoomJob::class.java)
            .withIdentity("studyRoomJob", "studyroom")
            .withDescription("Reset all study rooms daily")
            .storeDurably()
            .build()
    }

    @Bean
    fun studyRoomJobTrigger(studyRoomJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(studyRoomJobDetail)
            .withIdentity("studyRoomJobTrigger", "studyroom")
            .withDescription("Every day at 5:00 AM (Asia/Seoul)")
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0 0 5 * * ?")
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
    fun volunteerScoreJobDetail(): JobDetail {
        return JobBuilder.newJob(VolunteerScoreJob::class.java)
            .withIdentity("volunteerScoreJob", "volunteer")
            .withDescription("Convert volunteer score to point monthly")
            .storeDurably()
            .build()
    }

    @Bean
    fun volunteerScoreJobTrigger(volunteerScoreJobDetail: JobDetail): Trigger {
        return TriggerBuilder.newTrigger()
            .forJob(volunteerScoreJobDetail)
            .withIdentity("volunteerScoreJobTrigger", "volunteer")
            .withDescription("Every 28th day of month at midnight (Asia/Seoul)")
            .withSchedule(
                CronScheduleBuilder.cronSchedule("0 0 0 28 * ?")
                    .inTimeZone(java.util.TimeZone.getTimeZone("Asia/Seoul"))
            )
            .build()
    }
}
