package team.aliens.dms.scheduler.quartz.job

import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notice.dto.VoteResultNoticeRequest
import team.aliens.dms.domain.notice.usecase.VoteResultNoticeUseCase
import java.time.LocalDateTime
import java.util.UUID

@Component
class VoteResultNoticeJob(
    private val voteResultNoticeUseCase: VoteResultNoticeUseCase,
) : Job {
    override fun execute(context: JobExecutionContext?) {
        val votingTopicId: UUID = UUID.fromString(context!!.jobDetail.key.name)
        val startTime: LocalDateTime = LocalDateTime.parse(context.jobDetail.jobDataMap.get("startTime") as String)
        val isReNotice: Boolean = context.jobDetail.jobDataMap.get("isReNotice") as Boolean
        val managerId: UUID = UUID.fromString(context.jobDetail.jobDataMap.get("managerId") as String)
        val schoolId: UUID = UUID.fromString(context.jobDetail.jobDataMap.get("schoolId") as String)

        voteResultNoticeUseCase.execute(
            VoteResultNoticeRequest(
                votingTopicId,
                startTime,
                isReNotice,
                managerId,
                schoolId,
            )
        )
    }
}
