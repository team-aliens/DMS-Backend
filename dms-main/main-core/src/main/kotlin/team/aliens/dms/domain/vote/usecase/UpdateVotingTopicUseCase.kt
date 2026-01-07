package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.vote.dto.request.UpdateVotingTopicRequest
import team.aliens.dms.domain.vote.exception.InvalidPeriodException
import team.aliens.dms.domain.vote.service.VoteService
import java.time.LocalDateTime

@UseCase
class UpdateVotingTopicUseCase(
    private val voteService: VoteService,
    private val noticeService: NoticeService,
    private val securityService: SecurityService
) {

    fun execute(request: UpdateVotingTopicRequest) {
        if (request.startTime.isAfter(request.endTime) || request.endTime.isBefore(LocalDateTime.now())) {
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

        val managerId = securityService.getCurrentUserId()
        val schoolId = securityService.getCurrentSchoolId()

        noticeService.cancelVoteResultNotice(votingTopic.id)
        noticeService.scheduleVoteResultNotice(votingTopic.id, request.endTime, isReNotice, managerId, schoolId)
    }
}
