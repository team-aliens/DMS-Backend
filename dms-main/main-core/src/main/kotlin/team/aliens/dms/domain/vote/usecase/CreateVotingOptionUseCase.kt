package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.dto.request.CreateVotingOptionRequest
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.service.VoteService

@UseCase
class CreateVotingOptionUseCase(
    private val voteService: VoteService
) {

    fun execute(request: CreateVotingOptionRequest) {
        val votingTopic = voteService.getVotingTopicById(request.votingTopicId)
        voteService.checkTypeIsOptionVote(votingTopic)
        voteService.createVotingOption(
            VotingOption(
                votingTopicId = votingTopic.id,
                optionName = request.name
            )
        )
    }
}
