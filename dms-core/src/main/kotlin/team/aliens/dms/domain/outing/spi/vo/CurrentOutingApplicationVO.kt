package team.aliens.dms.domain.outing.spi.vo

import team.aliens.dms.domain.outing.model.OutingStatus
import java.time.LocalDate
import java.time.LocalTime

open class CurrentOutingApplicationVO(
    val outAt: LocalDate,
    val outingTypeTitle: String,
    val status: OutingStatus,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val outingCompanions: List<String>
)
