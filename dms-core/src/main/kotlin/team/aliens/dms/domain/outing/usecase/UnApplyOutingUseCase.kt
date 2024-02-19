package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.outing.service.OutingService
import java.util.UUID

@UseCase
class UnApplyOutingUseCase(
    private val outingService: OutingService
) {

    fun execute(outingApplicationId: UUID) {
        val outing = outingService.getOutingApplicationById(outingApplicationId)

        outingService.getOutingApplicationById(outingApplicationId)
            .apply {
                checkOutingType(outing.status)
            }

        outingService.deleteOutingApplication(outing)
    }
}
