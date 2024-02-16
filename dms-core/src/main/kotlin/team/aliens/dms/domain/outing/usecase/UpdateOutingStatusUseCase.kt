package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.service.OutingService
import java.util.UUID

@UseCase
class UpdateOutingStatusUseCase(
    private val outingService: OutingService
) {

    fun execute(outingApplicationId: UUID, outingStatus: OutingStatus) {
        val outingApplication = outingService.getOutingApplicationById(outingApplicationId)

        outingService.saveOutingApplication(
            outingApplication.copy(status = outingStatus)
        )
    }
}