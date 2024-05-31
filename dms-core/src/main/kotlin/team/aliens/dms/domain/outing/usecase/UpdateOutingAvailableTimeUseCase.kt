package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.outing.model.OutingAvailableTime
import team.aliens.dms.domain.outing.service.OutingService
import java.time.LocalTime
import java.util.UUID

@UseCase
class UpdateOutingAvailableTimeUseCase(
    private val outingService: OutingService
) {

    fun execute(outingAvailableTimeId: UUID, outingTime: LocalTime, arrivalTime: LocalTime) {
        val currentOutingAvailableTime = outingService.getOutingAvailableTimeById(outingAvailableTimeId)

        outingService.checkOutingAvailableTimeOverlap(
            dayOfWeek = currentOutingAvailableTime.dayOfWeek,
            outingTime = outingTime,
            arrivalTime = arrivalTime
        )

        val updatedOutingAvailableTime = OutingAvailableTime(
            id = outingAvailableTimeId,
            schoolId = currentOutingAvailableTime.schoolId,
            outingTime = outingTime,
            arrivalTime = arrivalTime,
            enabled = currentOutingAvailableTime.enabled,
            dayOfWeek = currentOutingAvailableTime.dayOfWeek
        )

        outingService.saveOutingAvailableTime(updatedOutingAvailableTime)
    }
}
