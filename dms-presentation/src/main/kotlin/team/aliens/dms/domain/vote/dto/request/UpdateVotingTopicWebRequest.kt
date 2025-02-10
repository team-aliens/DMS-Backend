package team.aliens.dms.domain.vote.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import team.aliens.dms.domain.vote.model.VoteType
import java.time.LocalDateTime
import java.util.UUID

data class UpdateVotingTopicWebRequest(

    @field:NotNull
    val id: UUID,

    @field:NotBlank(message = "Please provide a topic")
    @JsonProperty("topic_name") // JSON 필드명을 강제 지정
    val topicName: String?,

    @field:NotBlank(message = "Please provide a topic")
    val description: String?,

    @NotNull(message = "Please provide a topic")
    @JsonProperty("vote_type")
    val voteType: VoteType?,

    @field:NotNull(message = "Please provide a topic")
    @JsonProperty("start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val startTime: LocalDateTime?,

    @field:NotNull(message = "Please provide a topic")
    @JsonProperty("end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val endTime: LocalDateTime?,

)
