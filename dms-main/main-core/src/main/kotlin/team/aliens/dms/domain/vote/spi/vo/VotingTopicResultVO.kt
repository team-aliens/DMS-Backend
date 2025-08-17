package team.aliens.dms.domain.vote.spi.vo

import team.aliens.dms.domain.vote.model.VoteType
import java.time.LocalDateTime
import java.util.UUID

open class VotingTopicResultVO(
    val id: UUID,

    val topicName: String,

    val description: String?,

    val startTime: LocalDateTime,

    val endTime: LocalDateTime,

    val voteType: VoteType,

    val isVoted: Boolean
)
