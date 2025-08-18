package team.aliens.dms.domain.remain.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.remain.exception.RemainCanNotAppliedException
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Aggregate
data class RemainAvailableTime(

    val id: UUID,

    val startDayOfWeek: DayOfWeek,

    val startTime: LocalTime,

    val endDayOfWeek: DayOfWeek,

    val endTime: LocalTime

) {

    fun checkAvailable(currentDateTime: LocalDateTime) {
        val dayOfWeek = currentDateTime.dayOfWeek.value
        val now = currentDateTime.toLocalTime()

        if (isOutOfRangeDay(dayOfWeek) || isBeforeStartTime(dayOfWeek, now) || isAfterEndTime(dayOfWeek, now)) {
            throw RemainCanNotAppliedException
        }
    }

    private fun isOutOfRangeDay(dayOfWeek: Int) =
        (dayOfWeek < startDayOfWeek.value || dayOfWeek > endDayOfWeek.value)

    private fun isBeforeStartTime(dayOfWeek: Int, now: LocalTime) =
        (dayOfWeek == startDayOfWeek.value && now < startTime)

    private fun isAfterEndTime(dayOfWeek: Int, now: LocalTime) =
        (dayOfWeek == endDayOfWeek.value && endTime < now)
}
