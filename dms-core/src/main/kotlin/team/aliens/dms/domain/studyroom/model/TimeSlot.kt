package team.aliens.dms.domain.studyroom.model

import team.aliens.dms.common.model.SchoolIdDomain
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class TimeSlot(

    val id: UUID = UUID(0, 0),

    override val schoolId: UUID,

    val startTime: LocalTime,

    val endTime: LocalTime

) : SchoolIdDomain {
    val name: String = "${startTime.format(TIME_PATTERN)} ~ ${endTime.format(TIME_PATTERN)}"

    companion object {
        val TIME_PATTERN: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}
