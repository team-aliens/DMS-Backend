package team.aliens.dms.domain.vote.dto.response

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

data class ExcludedStudentResponse(
    val id: UUID,
    val gcn: String,
    val name: String
) {
    companion object {
        fun of(id: UUID, gcn: String, name: String): ExcludedStudentResponse {
            return ExcludedStudentResponse(
                id = id,
                gcn = gcn,
                name = name
            )
        }
    }
}

data class ExcludedStudentsResponses(
    val excludedStudents: List<ExcludedStudentResponse>
)

data class ModelStudentResponse(
    val id: UUID,
    val gcn: String,
    val name: String,
    val profileImageUrl: String?

)

data class ModelStudentsResponse(
    val students: List<ModelStudentResponse>

)

data class VoteResponse(
    val id: UUID,
    val name: String,
    val votes: Int,
    val classNumber: String?
) {
    companion object {
        fun of(
            id: UUID,
            name: String,
            votes: Int,
            classNumber: String?
        ) = VoteResponse(
            id = id,
            name = name,
            votes = votes,
            classNumber = classNumber
        )
    }
}

data class VotesResponse(
    val votes: List<VoteResponse>
) {
    companion object {
        fun of(
            votes: List<VoteResponse>
        ) = VotesResponse(
            votes = votes
        )
    }
}

class VotingOption(
    val id: UUID,
    val votingOptionName: String,
) {
    companion object {
        fun of(
            id: UUID,
            votingOptionName: String
        ) = VotingOption(
            id = id,
            votingOptionName = votingOptionName
        )
    }
}

class VotingOptionsResponse(
    val votingOptions: List<VotingOption>
) {
    companion object {
        fun of(
            votingOptions: List<VotingOption>
        ) = VotingOptionsResponse(
            votingOptions = votingOptions
        )
    }
}
