package team.aliens.dms.domain.daybreak.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.util.UUID

data class ApplyDaybreakStudyApplicationWebRequest(

    @field:NotNull
    val teacherId: UUID,

    @field:NotNull
    val typeId: UUID,

    @field:NotBlank
    @field:Size(max = 200)
    val reason: String,

    @field:NotNull
    val startDate: LocalDate,

    @field:NotNull
    val endDate: LocalDate,
)