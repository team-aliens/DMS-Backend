package team.aliens.dms.scheduler.quartz

import org.quartz.JobBuilder
import org.quartz.JobKey
import org.quartz.Scheduler
import org.quartz.TriggerBuilder
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.OnetimeSchdulerNoticePort
import team.aliens.dms.scheduler.quartz.job.VoteResultNoticeJob
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID

@Component
class QuartzSchedulerAdapter(
    private val scheduler: Scheduler
) : OnetimeSchdulerNoticePort {
    override fun scheduleVoteResultNotice(
        votingTopicId: UUID,
        startTime: LocalDateTime,
        isReNotice: Boolean,
        managerId: UUID,
        schoolId: UUID
    ) {

        val jobDetail = JobBuilder.newJob(VoteResultNoticeJob::class.java)
            .withIdentity(votingTopicId.toString(), "notice")
            .usingJobData("startTime", startTime.toString())
            .usingJobData("isReNotice", isReNotice)
            .usingJobData("managerId", managerId.toString())
            .usingJobData("schoolId", schoolId.toString())
            .build()

        val trigger = TriggerBuilder.newTrigger()
            .withIdentity(votingTopicId.toString(), "notice")
            .startAt(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()))
            .build()

        scheduler.scheduleJob(jobDetail, trigger)
    }

    override fun cancelVoteResultNotice(votingTopicId: UUID) {
        scheduler.deleteJob(JobKey(votingTopicId.toString(), "notice"))
    }
}
