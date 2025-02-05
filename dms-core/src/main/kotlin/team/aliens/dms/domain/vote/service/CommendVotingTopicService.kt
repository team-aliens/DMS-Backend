package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.model.VotingTopic
import java.util.UUID


interface CommendVotingTopicService {
    fun saveVoteTopic(voteTopic: VotingTopic)
    fun deleteVoteTopicById(id: UUID)
}