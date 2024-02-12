package team.aliens.dms.domain.outing.dto

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class ApplyOutingRequest(
    val outAt: LocalDate,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val destination: String,
    val outingTypeTitle: String,
    val reason: String,
    val companionIds: List<UUID>?,
)
