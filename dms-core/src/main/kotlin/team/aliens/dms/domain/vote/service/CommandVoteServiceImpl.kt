package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.CommandVotePort
import java.util.UUID

@Service
class CommandVoteServiceImpl(
    val commandVotePort: CommandVotePort
) : CommandVoteService {

    override fun saveVotingTopic(voteTopic: VotingTopic): VotingTopic {
        return commandVotePort.saveVotingTopic(voteTopic)
    }

    override fun deleteVotingTopicById(votingTopicId: UUID) {
        commandVotePort.deleteVotingTopicById(votingTopicId)
    }
}
