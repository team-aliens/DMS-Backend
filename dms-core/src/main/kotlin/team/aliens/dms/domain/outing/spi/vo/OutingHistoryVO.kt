package team.aliens.dms.domain.outing.spi.vo

import java.time.LocalTime
import java.util.UUID

open class OutingHistoryVO(
    val id: UUID,
    val studentGcn: String,
    val studentName: String,
    val outingType: String,
    val outingTime: LocalTime,
    val arrivalTime: LocalTime,
    val isApproved: Boolean,
    val isComeback: Boolean
)
