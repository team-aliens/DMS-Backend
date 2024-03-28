package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.outing.dto.OutingHistoryDetailsResponse
import team.aliens.dms.domain.outing.service.OutingService
import java.util.UUID

@ReadOnlyUseCase
class GetOutingHistoryDetailsUseCase(
    private val outingService: OutingService
) {

    fun execute(outingApplicationId: UUID): OutingHistoryDetailsResponse {
        val outingHistory = outingService.getOutingApplicationById(outingApplicationId)

        val outingCompanions = outingService.getOutingCompanionsByApplicationId(outingApplicationId)

        return OutingHistoryDetailsResponse.of(outingHistory, outingCompanions)
    }
}
