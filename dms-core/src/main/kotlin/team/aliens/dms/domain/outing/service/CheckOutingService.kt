package team.aliens.dms.domain.outing.service

import team.aliens.dms.domain.outing.model.OutingType
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

interface CheckOutingService {

    fun checkOutingApplicationAvailable(
        studentId: UUID,
        outingDate: LocalDate,
        outingTime: LocalTime,
        arrivalTime: LocalTime
    )

    fun checkOutingTypeExists(outingType: OutingType)

    fun checkOutingAvailableTimeOverlap(
        dayOfWeek: DayOfWeek,
        outingTime: LocalTime,
        arrivalTime: LocalTime
    )

    fun checkQueryAble() : Boolean
}
