package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.notice.service.CommandNoticeService
import team.aliens.dms.domain.vote.dto.request.CreateVoteTopicRequest
import team.aliens.dms.domain.vote.exception.InvalidPeriodException
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.VoteService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class CreateVotingTopicUseCase(
    private val voteService: VoteService,
    private val securityPort: SecurityPort,
    private val noticeService: CommandNoticeService
) {

    fun execute(request: CreateVoteTopicRequest): UUID {
        if (request.startTime.isAfter(request.endTime) || request.endTime.isBefore(LocalDateTime.now())) {
            throw InvalidPeriodException
        }

        val managerId = securityPort.getCurrentUserId()

        val votingTopicId = voteService.saveVotingTopic(
            VotingTopic(
                managerId = managerId,
                topicName = request.topicName,
                description = request.description,
                startTime = request.startTime,
                endTime = request.endTime,
                voteType = request.voteType,
            )
        ).id

        noticeService.scheduleVoteResultNotice(votingTopicId, request.endTime, false)

        return votingTopicId;
    }
}
