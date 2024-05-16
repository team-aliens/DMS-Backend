package team.aliens.dms.domain.outing.dto.request

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class ApplyOutingRequest(
    val outingDate: LocalDate,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val outingTypeTitle: String,
    val reason: String?,
    val companionIds: List<UUID>?
)
