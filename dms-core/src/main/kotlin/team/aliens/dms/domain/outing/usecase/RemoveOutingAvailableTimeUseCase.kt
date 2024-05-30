package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.outing.service.OutingService
import java.util.UUID

@UseCase
class RemoveOutingAvailableTimeUseCase(
    private val outingService: OutingService
) {

    fun execute(outingAvailableTimeId: UUID) {
        val outingAvailableTime = outingService.getOutingAvailableTimeById(outingAvailableTimeId)

        outingService.deleteOutingAvailableTime(outingAvailableTime)
    }
}
