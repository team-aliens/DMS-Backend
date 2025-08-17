package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.outing.service.OutingService
import java.util.UUID

@UseCase
class ToggleOutingAvailableTimeUseCase(
    private val outingService: OutingService
) {

    fun execute(outingAvailableTimeId: UUID) {
        val currentOutingAvailableTime = outingService.getOutingAvailableTimeById(outingAvailableTimeId)

        val toggledOutingAvailableTime = currentOutingAvailableTime.toggleEnabled()

        outingService.saveOutingAvailableTime(toggledOutingAvailableTime)
    }
}
