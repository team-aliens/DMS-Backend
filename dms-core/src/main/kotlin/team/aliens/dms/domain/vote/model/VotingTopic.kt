package team.aliens.dms.domain.vote.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.*

@Aggregate
data class VotingTopic(
    val id: UUID = UUID(0, 0),

    val topicName: String,

    val voteDescription: String,

    val startTime: LocalDateTime,

    val endTime: LocalDateTime,

    val voteType: VoteType,

    val managerId: UUID,
) {
}