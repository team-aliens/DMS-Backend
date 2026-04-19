package team.aliens.dms.domain.daybreak.dto.request

import java.time.LocalDate
import java.util.UUID

data class ApplyDaybreakStudyApplicationRequest(
    val teacherId: UUID,
    val typeId: UUID,
    val reason: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)
