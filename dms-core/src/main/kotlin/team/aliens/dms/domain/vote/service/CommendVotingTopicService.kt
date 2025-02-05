package team.aliens.dms.domain.vote.service

import team.aliens.dms.domain.vote.model.VotingTopic


interface CommendVotingTopicService {
    fun saveVoteTopic(voteTopic: VotingTopic)
}