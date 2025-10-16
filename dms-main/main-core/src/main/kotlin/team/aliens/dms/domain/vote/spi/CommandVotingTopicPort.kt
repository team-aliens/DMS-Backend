package team.aliens.dms.domain.vote.spi

import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface CommandVotingTopicPort {

    fun saveVotingTopic(votingTopic: VotingTopic): VotingTopic

    fun deleteVotingTopicById(votingTopicId: UUID)
}
