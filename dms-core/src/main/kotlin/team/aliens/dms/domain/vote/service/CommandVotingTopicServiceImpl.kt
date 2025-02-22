package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.CommandVotingTopicPort
import java.util.UUID

@Service
class CommandVotingTopicServiceImpl(
    val commandVotingTopicPort: CommandVotingTopicPort
) : CommandVotingTopicService {

    override fun saveVotingTopic(voteTopic: VotingTopic): VotingTopic {
        return commandVotingTopicPort.saveVotingTopic(voteTopic)
    }

    override fun deleteVotingTopicById(votingTopicId: UUID) {
        commandVotingTopicPort.deleteVotingTopicById(votingTopicId)
    }
}
