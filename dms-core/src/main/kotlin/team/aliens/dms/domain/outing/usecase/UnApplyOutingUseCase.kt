package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.outing.exception.OutingTypeMismatchException
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.service.OutingService
import java.util.UUID

@UseCase
class UnApplyOutingUseCase(
    private val outingService: OutingService
) {

    fun execute(outingApplicationId: UUID) {
        val outing = outingService.getOutingById(outingApplicationId)

        if (outing.status != OutingStatus.REQUESTED) {
            throw OutingTypeMismatchException
        }

        outingService.deleteOutingApplication(outing)
    }
}
