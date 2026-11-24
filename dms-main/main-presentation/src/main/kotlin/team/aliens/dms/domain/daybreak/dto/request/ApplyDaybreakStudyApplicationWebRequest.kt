package team.aliens.dms.domain.daybreak.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
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

    // 월~목 중 고른 날짜들. 연속하지 않아도 된다(예: 월·수)
    @field:NotEmpty
    @field:Size(max = 4)
    val dates: List<LocalDate>,
)
