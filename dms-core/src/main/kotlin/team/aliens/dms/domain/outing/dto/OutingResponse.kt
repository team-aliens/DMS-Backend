package team.aliens.dms.domain.outing.dto

import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.model.OutingType
import java.time.LocalDate
import java.time.LocalTime

data class GetAllOutingTypeTitlesResponse(
    val titles: List<String>
)

data class ExportAllOutingApplicationsResponse(
    val file: ByteArray,
    val fileName: String
)

data class CurrentOutingApplicationResponse(
    val outAt: LocalDate,
    val outingType: OutingType,
    val status: OutingStatus,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val students: List<String>,
    val reason: String
)
