package team.aliens.dms.domain.remain.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Aggregate
data class RemainAvailableTime(

    val id: UUID,

    val startTime: LocalTime,

    val startDayOfWeek: DayOfWeek,

    val endTime: LocalTime,

    val endDayOfWeek: DayOfWeek

) {

    fun isAccessible(): Boolean {
        val currentDateTime = LocalDateTime.now()
        val dayOfWeek = currentDateTime.dayOfWeek.value
        val now = currentDateTime.toLocalTime()

        if (dayOfWeek < startDayOfWeek.value || dayOfWeek > endDayOfWeek.value) {
            return false
        }

        if (now < startTime || now > endTime) {
            return false
        }

        return true
    }
}