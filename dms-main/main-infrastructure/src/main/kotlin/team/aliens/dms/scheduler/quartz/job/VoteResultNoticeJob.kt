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
    override fun execute(context: JobExecutionContext) {
        val dataMap = context.jobDetail.jobDataMap
        val votingTopicId: UUID = UUID.fromString(context.jobDetail.key.name)
        val startTime: LocalDateTime = LocalDateTime.parse(dataMap.getString("startTime"))
        val isReNotice: Boolean = dataMap.getBoolean("isReNotice")
        val managerId: UUID = UUID.fromString(dataMap.getString("managerId"))
        val schoolId: UUID = UUID.fromString(dataMap.getString("schoolId"))

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
