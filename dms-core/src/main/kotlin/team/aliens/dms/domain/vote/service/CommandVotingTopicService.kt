package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface CommandVotingTopicService {

    fun saveVotingTopic(voteTopic: VotingTopic): VotingTopic

    fun deleteVotingTopicById(votingTopicId: UUID)
}
