package team.aliens.dms.domain.vote.dto

import team.aliens.dms.domain.vote.model.VoteType
import java.time.LocalDateTime

data class CreateVoteTopicRequest(
    val topicName: String,
    val voteDescription: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val voteType: VoteType
)
