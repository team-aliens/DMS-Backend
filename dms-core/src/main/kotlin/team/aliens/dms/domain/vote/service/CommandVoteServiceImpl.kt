package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.exception.VotingTopicAlreadyExistException
import team.aliens.dms.domain.vote.model.Vote
import team.aliens.dms.domain.vote.model.VotingOption
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.CommandVotePort
import team.aliens.dms.domain.vote.spi.CommandVotingOptionPort
import team.aliens.dms.domain.vote.spi.CommandVotingTopicPort
import team.aliens.dms.domain.vote.spi.QueryVotePort
import team.aliens.dms.domain.vote.spi.QueryVotingTopicPort
import java.util.UUID

@Service
class CommandVoteServiceImpl(
    val queryVotePort: QueryVotePort,
    val commandVotePort: CommandVotePort,
    val commandVotingTopicPort: CommandVotingTopicPort,
    val queryVotingTopicPort: QueryVotingTopicPort,
    val commandVotingOptionPort: CommandVotingOptionPort
) : CommandVoteService {

    override fun saveVotingTopic(voteTopic: VotingTopic): VotingTopic {
        return commandVotingTopicPort.saveVotingTopic(voteTopic)
    }

    override fun deleteVotingTopicById(votingTopicId: UUID) {
        commandVotingTopicPort.deleteVotingTopicById(votingTopicId)
    }

    override fun createVotingTopic(votingTopic: VotingTopic): VotingTopic {
        if (checkVotingTopic(votingTopic.topicName)) {
            throw VotingTopicAlreadyExistException
        }
        return commandVotingTopicPort.saveVotingTopic(votingTopic)
    }

    override fun createVote(vote: Vote): Vote = commandVotePort.saveVote(vote)

    override fun createVotingOption(votingOption: VotingOption): VotingOption {
        return commandVotingOptionPort.saveVotingOption(votingOption)
    }

    override fun deleteVotingTopic(votingTopicId: UUID) = commandVotingTopicPort.deleteVotingTopicById(votingTopicId)

    override fun deleteVotingOptionById(
        votingOptionId: UUID
    ) = commandVotingOptionPort.deleteVotingOptionByVotingOptionId(votingOptionId)

    override fun deleteVote(voteId: UUID) = commandVotePort.deleteVoteById(voteId)

    override fun checkVotingTopic(name: String): Boolean = queryVotingTopicPort.existVotingTopicByName(name)
}
