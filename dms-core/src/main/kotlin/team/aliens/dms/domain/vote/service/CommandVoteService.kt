package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID

interface CommandVoteService {

    fun saveVotingTopic(voteTopic: VotingTopic): VotingTopic

    fun deleteVotingTopicById(votingTopicId: UUID)
}
