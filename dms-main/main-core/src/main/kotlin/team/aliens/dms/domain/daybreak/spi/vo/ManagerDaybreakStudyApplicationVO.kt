package team.aliens.dms.domain.daybreak.spi.vo

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

open class ManagerDaybreakStudyApplicationVO(
    val applicationId: UUID,
    val typeName: String,
    val teacherName: String,
    val createdAt: LocalDateTime,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val reason: String,
    val studentName: String,
    val studentGcn: String,
)
