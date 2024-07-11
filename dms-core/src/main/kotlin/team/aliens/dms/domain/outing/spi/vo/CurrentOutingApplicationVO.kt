package team.aliens.dms.domain.outing.spi.vo

import team.aliens.dms.domain.outing.model.OutingStatus
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

open class CurrentOutingApplicationVO(
    val id: UUID,
    val outingDate: LocalDate,
    val outingTypeTitle: String,
    val status: OutingStatus,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val reason: String?,
    val outingApplication: String,
    val outingCompanions: List<String>
)
