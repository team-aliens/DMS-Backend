package team.aliens.dms.domain.outing.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import team.aliens.dms.domain.outing.model.OutingType
import java.time.LocalDate
import java.time.LocalTime

data class CreateOutingWebRequest(
    @field:NotNull
    val outAt: LocalDate,

    @field:NotNull
    val outingTime: LocalTime,

    @field:NotNull
    val arrivalTime: LocalTime,

    @field:NotNull
    @field:Size(max = 15)
    @NotBlank
    val destination: String,

    @field:NotNull
    val outingTypeId: OutingType,

    @field:NotNull
    @field:Size(max = 100)
    @NotBlank
    val reason: String
)
