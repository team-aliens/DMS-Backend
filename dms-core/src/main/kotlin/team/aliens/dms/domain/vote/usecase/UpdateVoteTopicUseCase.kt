package team.aliens.dms.domain.vote.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.vote.dto.request.UpdateVotingTopicRequest
import team.aliens.dms.domain.vote.service.CommendVotingTopicService
import team.aliens.dms.domain.vote.service.GetVotingTopicService

@UseCase
class UpdateVoteTopicUseCase(
    private val commandVotingTopicService: CommendVotingTopicService,
    private val getVotingTopicService: GetVotingTopicService
) {

    fun execute(request: UpdateVotingTopicRequest){

        val votingTopic = getVotingTopicService.getVotingTopicById(request.id)
        commandVotingTopicService.saveVoteTopic(votingTopic!!.copy(
            topicName = request.topicName,
            voteType = request.voteType,
            description = request.description,
            startTime = request.startTime,
            endTime = request.endTime
        ))
    }

}