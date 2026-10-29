package team.aliens.dms.domain.daybreak.spi.vo

import team.aliens.dms.domain.daybreak.model.Status
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

open class DaybreakStudyApplicationVO(
    val applicationId: UUID,
    val typeName: String,
    val createdAt: LocalDateTime,
    val dates: List<LocalDate>,
    val reason: String,
    val studentId: UUID?,
    val studentName: String,
    val studentGcn: String,
    val teacherName: String? = null,
    val status: Status? = null
) {
    // 구간으로 표시하는 구버전 클라이언트 호환용(#1138). 구버전이 사라지면 제거한다
    val startDate: LocalDate
        get() = dates.first()

    val endDate: LocalDate
        get() = dates.last()
}
