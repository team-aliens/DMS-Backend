package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notice.service.CommandNoticeService
import team.aliens.dms.domain.vote.dto.request.CreateVoteTopicRequest
import team.aliens.dms.domain.vote.dto.response.CreateVotingTopicResponse
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.VoteService

@UseCase
class CreateVotingTopicUseCase(
    private val voteService: VoteService,
    private val securityService: SecurityService,
    private val noticeService: CommandNoticeService
) {

    companion object {
        val APPROVAL = "찬성"
        val OPPOSITE = "반대"
    }

    fun execute(request: CreateVoteTopicRequest): CreateVotingTopicResponse {
        val managerId = securityService.getCurrentUserId()

        val votingTopic = voteService.saveVotingTopic(
            VotingTopic(
                managerId = managerId,
                topicName = request.topicName,
                description = request.description,
                startTime = request.startTime,
                endTime = request.endTime,
                voteType = request.voteType,
            )
        )
        votingTopic.checkVotingTopicPeriod()

        if (votingTopic.voteType == VoteType.APPROVAL_VOTE) {
            addApprovalOptions(votingTopic)
        }

        noticeService.scheduleVoteResultNotice(votingTopic.id, request.endTime, false)

        return CreateVotingTopicResponse(votingTopic.id)
    }

    private fun addApprovalOptions(votingTopic: VotingTopic) {
        voteService.createVotingOption(
            VotingOption(
                votingTopicId = votingTopic.id,
                optionName = APPROVAL
            )
        )

        voteService.createVotingOption(
            VotingOption(
                votingTopicId = votingTopic.id,
                optionName = OPPOSITE
            )
        )
    }
}
