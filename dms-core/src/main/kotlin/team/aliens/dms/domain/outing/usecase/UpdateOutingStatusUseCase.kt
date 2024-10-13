package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.outing.service.OutingService
import java.util.UUID

@UseCase
class UpdateOutingStatusUseCase(
    private val outingService: OutingService
) {

    fun execute(outingApplicationId: UUID, isApproved: Boolean, isComeback: Boolean) {
        val outingApplication = outingService.getOutingApplicationById(outingApplicationId)

        outingService.saveOutingApplication(
            outingApplication.copy(isApproved = isApproved, isComeback = isComeback)
        )
    }
}
