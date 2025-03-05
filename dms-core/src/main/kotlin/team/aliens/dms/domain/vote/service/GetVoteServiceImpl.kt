package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.exception.VotingTopicNotFoundException
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.QueryVotePort
import java.util.UUID

@Service
class GetVoteServiceImpl(
    private val queryVotePort: QueryVotePort
) : GetVoteService {

    override fun getVotingTopicById(votingTopicId: UUID): VotingTopic {
        return queryVotePort.queryVotingTopicById(votingTopicId) ?: throw VotingTopicNotFoundException
    }

    override fun getAllVotingTopics(): List<VotingTopic> {
        return queryVotePort.queryAllVotingTopic()
    }
}
