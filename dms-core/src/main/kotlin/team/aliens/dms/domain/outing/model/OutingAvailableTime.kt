package team.aliens.dms.domain.outing.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.domain.outing.exception.OutingAvailableTimeMismatchException
import team.aliens.dms.domain.point.dto.PointRequestType
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

@Aggregate
class OutingAvailableTime(

    val id: UUID = UUID(0, 0),

    override val schoolId: UUID,

    val outingTime: LocalTime,

    val arrivalTime: LocalTime,

    val enabled: Boolean,

    val dayOfWeek: DayOfWeek,

) : SchoolIdDomain {

    fun checkAvailable(
        dayOfWeek: DayOfWeek,
        outingTime: LocalTime,
        arrivalTime: LocalTime,
    ) {
        if (this.dayOfWeek != dayOfWeek ||
            this.outingTime.isAfter(outingTime) ||
            this.arrivalTime.isBefore(arrivalTime)
        ) {
            throw OutingAvailableTimeMismatchException
        }
    }

    companion object {
        fun timesOverlap(
                newStartTime: LocalTime,
                newEndTime: LocalTime,
                startTime: LocalTime,
                endTime: LocalTime
        ): Boolean {
            return !(newEndTime <= startTime || newStartTime >= endTime)
        }
    }
}
