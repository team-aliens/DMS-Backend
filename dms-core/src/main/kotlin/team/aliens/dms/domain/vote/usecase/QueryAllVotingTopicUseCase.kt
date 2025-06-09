package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.service.UserService
import team.aliens.dms.domain.vote.dto.response.VotingTopicResponse
import team.aliens.dms.domain.vote.dto.response.VotingTopicsResponse
import team.aliens.dms.domain.vote.service.VoteService

@ReadOnlyUseCase
class QueryAllVotingTopicUseCase(
    val voteService: VoteService,
    val userService: UserService
) {

    fun execute(): VotingTopicsResponse {
        val user: User = userService.getCurrentUser()
        val voteTopic = voteService.getAllVotingTopics(user.id)

        return VotingTopicsResponse(
            voteTopic.map {
                VotingTopicResponse.from(it)
            }
        )
    }
}
