package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.vote.dto.reponse.GetVotingTopicResponse
import team.aliens.dms.domain.vote.service.GetVotingTopicService
import java.util.UUID

@ReadOnlyUseCase
class QueryVotingTopicUseCase(
    private val getVotingTopicService: GetVotingTopicService
) {

    fun execute(id: UUID) = GetVotingTopicResponse.from(getVotingTopicService.getVotingTopicById(id))
}
