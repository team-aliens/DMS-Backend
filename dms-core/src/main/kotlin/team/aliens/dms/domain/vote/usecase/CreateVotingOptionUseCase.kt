package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.dto.request.CreateVotingOptionRequest
import team.aliens.dms.domain.vote.exception.VoteTypeMismatchException
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.service.VoteService

@UseCase
class CreateVotingOptionUseCase(
    private val voteService: VoteService
) {

    fun execute(request: CreateVotingOptionRequest) {
        val votingTopic = voteService.getVotingTopicById(request.votingTopicId)
        if (votingTopic.voteType != VoteType.OPTION_VOTE) {
            throw VoteTypeMismatchException
        }
        voteService.createVotingOption(
            VotingOption(
                votingTopicId = votingTopic.id,
                optionName = request.name
            )
        )
    }
}
