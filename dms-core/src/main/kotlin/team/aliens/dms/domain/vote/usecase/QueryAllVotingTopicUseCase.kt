package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.vote.dto.reponse.GetAllVotingTopicResponse
import team.aliens.dms.domain.vote.service.VoteService

@ReadOnlyUseCase
class QueryAllVotingTopicUseCase(
    val voteService: VoteService
) {

    fun execute() = GetAllVotingTopicResponse.from(voteService.getAllVotingTopics())
}
