package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.exception.VotingTopicNotFoundException
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.QueryVotingTopicPort
import java.util.UUID

@Service
class GetVotingTopicServiceImpl(
    private val queryVotingTopicPort: QueryVotingTopicPort
) : GetVotingTopicService {

    override fun getVotingTopicById(votingTopicId: UUID): VotingTopic {
        return queryVotingTopicPort.queryVotingTopicById(votingTopicId) ?: throw VotingTopicNotFoundException
    }

    override fun getAllVotingTopics(): List<VotingTopic> {
        return queryVotingTopicPort.queryAllVotingTopic()
    }
}
