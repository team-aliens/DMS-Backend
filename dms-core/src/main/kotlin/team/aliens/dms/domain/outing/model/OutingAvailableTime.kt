package team.aliens.dms.domain.outing.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.domain.outing.exception.OutingAvailableTimeMismatchException
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

@Aggregate
data class OutingAvailableTime(

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

    fun timesOverlap(
        newOutingTime: LocalTime,
        newArrivalTime: LocalTime
    ): Boolean {
        return !(newArrivalTime <= this.outingTime || newOutingTime >= this.arrivalTime)
    }
}
