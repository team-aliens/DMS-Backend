package team.aliens.dms.domain.daybreak.dto.response

import team.aliens.dms.domain.daybreak.spi.vo.GeneralTeacherDaybreakStudyApplicationVO
import team.aliens.dms.domain.daybreak.spi.vo.HeadTeacherDaybreakStudyApplicationVO
import team.aliens.dms.domain.daybreak.spi.vo.ManagerDaybreakStudyApplicationVO
import java.util.UUID

data class GeneralTeacherDaybreakStudyApplicationsResponse(
    val applications: List<GeneralTeacherDaybreakStudyApplicationVO>
)

data class HeadTeacherDaybreakStudyApplicationsResponse(
    val applications: List<HeadTeacherDaybreakStudyApplicationVO>
)

data class ManagerDaybreakStudyApplicationsResponse(
    val applications: List<ManagerDaybreakStudyApplicationVO>
)

data class DaybreakStudyTypesResponse(
    val types: List<DaybreakStudyTypeResponse>
) {
    data class DaybreakStudyTypeResponse(
        val id: UUID,
        val name: String,
    )
}
