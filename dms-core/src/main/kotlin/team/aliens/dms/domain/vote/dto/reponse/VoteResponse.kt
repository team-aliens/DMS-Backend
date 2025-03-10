package team.aliens.dms.domain.vote.dto.reponse

import team.aliens.dms.domain.vote.model.VoteType
import team.aliens.dms.domain.vote.model.VotingTopic
import java.time.LocalDateTime
import java.util.UUID

data class VotingTopicResponse(
    val id: UUID,
    val topicName: String,
    val description: String? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val voteType: VoteType,
) {
    companion object {
        fun from(votingTopic: VotingTopic): VotingTopicResponse {
            return VotingTopicResponse(
                id = votingTopic.id,
                topicName = votingTopic.topicName,
                description = votingTopic.description,
                startTime = votingTopic.startTime,
                endTime = votingTopic.endTime,
                voteType = votingTopic.voteType,
            )
        }
    }
}

data class VotingTopicsResponse(
    val votingTopics: List<VotingTopicResponse>
)
