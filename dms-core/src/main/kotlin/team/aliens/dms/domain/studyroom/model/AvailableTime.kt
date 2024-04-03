package team.aliens.dms.domain.studyroom.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalTime
import java.util.UUID

@Aggregate
data class AvailableTime(

    val schoolId: UUID,

    val startAt: LocalTime,

    val endAt: LocalTime

) {
    fun isAvailable(now: LocalTime): Boolean {
        if ((startAt < endAt && (isBeforeStartTime(now) || isAfterEndTime(now))) ||
            startAt > endAt && (isBeforeStartTime(now) && isAfterEndTime(now))
        ) {
            return false
        }
        return true
    }

    private fun isBeforeStartTime(now: LocalTime) =
        (now < startAt)

    private fun isAfterEndTime(now: LocalTime) =
        (endAt < now)
}
