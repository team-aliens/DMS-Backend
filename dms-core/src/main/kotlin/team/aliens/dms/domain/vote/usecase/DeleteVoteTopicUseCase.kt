package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.service.CommendVotingTopicService
import java.util.UUID

@UseCase
class DeleteVoteTopicUseCase(
    private val commandVotingTopicService: CommendVotingTopicService
) {
    fun excute(id: UUID){
        commandVotingTopicService.deleteVoteTopicById(id)
    }
}