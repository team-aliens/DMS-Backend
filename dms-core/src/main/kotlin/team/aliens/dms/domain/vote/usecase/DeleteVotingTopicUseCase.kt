package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.domain.vote.service.CommendVotingTopicService
import java.util.UUID

@UseCase
class DeleteVotingTopicUseCase(
    private val commandVotingTopicService: CommendVotingTopicService,
    private val schedulerPort: TaskSchedulerPort
) {

    fun execute(votingTopicId: UUID) {
        commandVotingTopicService.deleteVotingTopicById(votingTopicId)
        schedulerPort.cancelTask(votingTopicId)
    }
}
