package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.vote.model.VotingTopic
import team.aliens.dms.domain.vote.spi.CommendVotingTopicPort
import java.util.*

@Service
class CommendVotingTopicServiceImpl(
    val commendVotingTopicPort: CommendVotingTopicPort
): CommendVotingTopicService {
    override fun saveVoteTopic(voteTopic: VotingTopic) {
        commendVotingTopicPort.saveVotingTopic(voteTopic)
    }

    override fun deleteVoteTopicById(id: UUID) {
        commendVotingTopicPort.deleteVotingTopicById(id)
    }
}