package team.aliens.dms.domain.studyroom.model

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class TimeSlot(

    val id: UUID = UUID(0, 0),

    val schoolId: UUID,

    val startTime: LocalTime,

    val endTime: LocalTime
) {
    val name: String = "${startTime.format(TIME_PATTERN)} ~ ${endTime.format(TIME_PATTERN)}"

    companion object {
        val TIME_PATTERN: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}
