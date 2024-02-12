package team.aliens.dms.domain.outing.dto

import team.aliens.dms.domain.outing.model.OutingType
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class CreateOutingApplicationRequest(
    val outAt: LocalDate,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val destination: String,
    val outingTypeId: OutingType,
    val reason: String,
    val companionId: List<UUID>
)
