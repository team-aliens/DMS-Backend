package team.aliens.dms.domain.daybreak.dto.response

import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationVO
import java.util.UUID

data class DaybreakStudyApplicationResponse(
    val applications: List<DaybreakStudyApplicationVO>
)

data class DaybreakStudyTypesResponse(
    val types: List<DaybreakStudyTypeResponse>
) {
    data class DaybreakStudyTypeResponse(
        val id: UUID,
        val name: String,
    )
}
