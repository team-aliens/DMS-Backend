package team.aliens.dms.domain.studyroom.dto

import java.time.LocalTime
import java.util.UUID

data class QueryTimeSlotsResponse(
    val timeSlots: List<TimeSlotElement>
) {
    data class TimeSlotElement(
        val id: UUID,
        val startTime: LocalTime,
        val endTime: LocalTime
    )
}
