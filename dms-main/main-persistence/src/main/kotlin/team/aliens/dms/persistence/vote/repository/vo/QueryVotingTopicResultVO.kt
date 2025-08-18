package team.aliens.dms.persistence.vote.repository.vo

import com.querydsl.core.annotations.QueryProjection
import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.spi.vo.VotingTopicResultVO
import java.time.LocalDateTime
import java.util.UUID

open class QueryVotingTopicResultVO @QueryProjection constructor(
    id: UUID,
    topicName: String,
    description: String?,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    voteType: VoteType,
    isVoted: Boolean
) : VotingTopicResultVO(
    id = id,
    topicName = topicName,
    description = description,
    startTime = startTime,
    endTime = endTime,
    voteType = voteType,
    isVoted = isVoted
)
