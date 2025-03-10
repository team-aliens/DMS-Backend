package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.dto.response.VotingOption
import team.aliens.dms.domain.vote.dto.response.VotingOptionsResponse
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.VoteService
import java.util.UUID

@UseCase
class QueryVotingOptionsUseCase(
    private val votingService: VoteService
) {

    fun execute(votingTopicId: UUID): VotingOptionsResponse {
        val votingTopic: VotingTopic = votingService.getVotingTopicById(votingTopicId)

        return VotingOptionsResponse(
            votingService.getVotingOptionsByVotingTopicId(votingTopic.id).map { option ->
                VotingOption(
                    id = option.id,
                    votingOptionName = option.optionName
                )
            }
        )
    }
}
