package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.outing.dto.OutingApplicationHistoryResponse
import team.aliens.dms.domain.outing.model.OutingStatus
import team.aliens.dms.domain.outing.service.OutingService
import java.time.LocalDate

@ReadOnlyUseCase
class GetOutingApplicationHistoryUseCase(
    private val outingService: OutingService,
) {

    fun execute(
        name: String?,
        date: LocalDate,
        outingStatus: OutingStatus
    ): OutingApplicationHistoryResponse {
        val outings = outingService.getOutingHistoryByNameAndDateAndStatus(
            name = name,
            date = date,
            outingStatus = outingStatus
        )

        return OutingApplicationHistoryResponse.of(outings)
    }
}
