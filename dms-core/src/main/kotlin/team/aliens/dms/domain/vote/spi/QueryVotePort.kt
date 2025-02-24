package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface QueryVotePort {

    fun queryVotingTopicById(votingTopicId: UUID): VotingTopic?

    fun queryAllVotingTopic(): List<VotingTopic>
}
