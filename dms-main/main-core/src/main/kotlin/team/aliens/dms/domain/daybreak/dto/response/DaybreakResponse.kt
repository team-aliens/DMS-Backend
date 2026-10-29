package team.aliens.dms.domain.daybreak.dto.response

import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.spi.vo.DaybreakStudyApplicationVO
import java.time.LocalDate
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

data class DaybreakStudyApplicationStatusResponse(
    val status: Status,
    val dates: List<LocalDate>,
    val typeName: String,
    val reason: String,
    val teacherName: String
) {
    // 구간으로 표시하는 구버전 학생 앱 호환용(#1138). 구버전이 사라지면 제거한다
    val startDate: LocalDate
        get() = dates.first()

    val endDate: LocalDate
        get() = dates.last()
}

data class ExportManagerDaybreakStudyApplicationResponse(
    val file: ByteArray,
    val fileName: String
)
