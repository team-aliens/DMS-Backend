package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.service.VoteService
import java.util.UUID

@UseCase
class DeleteVotingTopicUseCase(
    private val voteService: VoteService,
    private val schedulerPort: TaskSchedulerPort
) {

    fun execute(votingTopicId: UUID) {
        val votingTopic: VotingTopic = voteService.getVotingTopicById(votingTopicId)

        voteService.getVotingOptionsByVotingTopicId(votingTopicId).forEach {
                votingOption ->
            voteService.deleteVoteByVotingOption(votingOption)
        }
        voteService.deleteVotingOptionByVotingTopic(votingTopic)

        voteService.deleteVotingTopicById(votingTopicId)

        schedulerPort.cancelTask(votingTopicId)
    }
}
