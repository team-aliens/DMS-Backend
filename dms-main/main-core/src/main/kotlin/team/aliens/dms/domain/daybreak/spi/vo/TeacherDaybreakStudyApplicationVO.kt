package team.aliens.dms.domain.daybreak.spi.vo

import team.aliens.dms.domain.daybreak.model.Status
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

open class GeneralTeacherDaybreakStudyApplicationVO(
    val applicationId: UUID,
    val typeName: String,
    val createdAt: LocalDateTime,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val status: Status,
    val reason: String,
    val studentName: String,
    val studentGcn: String
)
