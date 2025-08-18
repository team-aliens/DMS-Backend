package team.aliens.dms.domain.outing.spi.vo

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

open class CurrentOutingApplicationVO(
    val id: UUID,
    val outingDate: LocalDate,
    val outingTypeTitle: String,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val reason: String?,
    val outingApplicant: String,
    val outingCompanions: List<String>
)
