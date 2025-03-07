package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface QueryVotingTopicPort {

    fun existVotingTopicByName(votingTopicName: String): Boolean

    fun existVotingTopicById(votingTopicId: UUID): Boolean

    fun queryVotingTopicById(votingTopicId: UUID): VotingTopic?

    fun queryAllVotingTopic(): List<VotingTopic>
}
