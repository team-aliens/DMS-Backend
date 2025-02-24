package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.domain.notice.service.CommandNoticeService
import team.aliens.dms.domain.vote.dto.request.UpdateVotingTopicRequest
import team.aliens.dms.domain.vote.exception.InvalidPeriodException
import team.aliens.dms.domain.vote.service.VoteService
import java.time.LocalDateTime

@UseCase
class UpdateVotingTopicUseCase(
    private val taskSchedulerPort: TaskSchedulerPort,
    private val voteService: VoteService,
    private val noticeService: CommandNoticeService
) {

    fun execute(request: UpdateVotingTopicRequest) {
        if (request.startTime.isAfter(request.endTime)) {
            throw InvalidPeriodException
        }

        val votingTopic = voteService.getVotingTopicById(request.id)
        val isReNotice = votingTopic.endTime.isBefore(LocalDateTime.now())

        voteService.saveVotingTopic(
            votingTopic.copy(
                topicName = request.topicName,
                voteType = request.voteType,
                description = request.description,
                startTime = request.startTime,
                endTime = request.endTime
            )
        )

        taskSchedulerPort.cancelTask(request.id)
        noticeService.scheduleVoteResultNotice(request.id, request.endTime, isReNotice)
    }
}
