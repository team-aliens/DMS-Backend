package team.aliens.dms.domain.vote.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import team.aliens.dms.domain.vote.model.VoteType
import java.time.LocalDateTime

data class CreateVotingTopicWebRequest(
    @field:NotBlank
    val topicName: String,

    val description: String,

    @field:NotNull
    val voteType: VoteType,

    @field:NotNull
    val startTime: LocalDateTime,

    @field:NotNull
    val endTime: LocalDateTime,
)
