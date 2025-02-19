package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.domain.notice.service.CommandNoticeService
import team.aliens.dms.domain.vote.dto.request.UpdateVotingTopicRequest
import team.aliens.dms.domain.vote.exception.NotValidPeriodException
import team.aliens.dms.domain.vote.service.VoteService
import java.time.LocalDateTime

@UseCase
class UpdateVotingTopicUseCase(
    private val taskSchedulerPort: TaskSchedulerPort,
    private val voteService: VoteService,
    private val noticeService: CommandNoticeService
) {

    fun execute(request: UpdateVotingTopicRequest) {
        if (request.startTime.isAfter(request.endTime) || request.endTime.isBefore(LocalDateTime.now())) {
            throw NotValidPeriodException
        }

        val votingTopic = voteService.getVotingTopicById(request.id)
        var isReNotice = if (votingTopic.endTime.isBefore(LocalDateTime.now())) true else false

        val savedVotingTopicId = voteService.saveVotingTopic(
            votingTopic.copy(
                topicName = request.topicName,
                voteType = request.voteType,
                description = request.description,
                startTime = request.startTime,
                endTime = request.endTime
            )
        )

        taskSchedulerPort.cancelTask(request.id)
        noticeService.scheduleVoteResultNotice(savedVotingTopicId, request.endTime, isReNotice)
    }
}
