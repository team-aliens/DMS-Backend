package team.aliens.dms.domain.outing.service

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

interface CheckOutingService {

    fun checkOutingApplicationAvailable(
        studentId: UUID,
        outAt: LocalDate,
        outingTime: LocalTime,
        arrivalTime: LocalTime
    )
}
