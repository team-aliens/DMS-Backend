package team.aliens.dms.domain.outing.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class ApplyOutingWebRequest(
    @field:NotNull
    val outAt: LocalDate,

    @field:NotNull
    val outingTime: LocalTime,

    @field:NotNull
    val arrivalTime: LocalTime,

    @field:Size(max = 15)
    @field:NotBlank
    val destination: String,

    @field:Size(max = 20)
    @field:NotBlank
    val outingTypeTitle: String,

    @field:Size(max = 100)
    @field:NotBlank
    val reason: String,

    val companionIds: List<UUID>?,
)
