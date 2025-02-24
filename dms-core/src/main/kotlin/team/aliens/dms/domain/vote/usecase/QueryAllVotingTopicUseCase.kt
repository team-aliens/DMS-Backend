package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.vote.dto.reponse.VotingTopicResponse
import team.aliens.dms.domain.vote.dto.reponse.VotingTopicsResponse
import team.aliens.dms.domain.vote.service.VoteService

@ReadOnlyUseCase
class QueryAllVotingTopicUseCase(
    val voteService: VoteService
) {

    fun execute(): VotingTopicsResponse {
        val voteTopic = voteService.getAllVotingTopics()

        return VotingTopicsResponse(
            voteTopic.map {
                VotingTopicResponse.from(it)
            }
        )
    }
}
