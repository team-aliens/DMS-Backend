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
        studentName: String?,
        date: LocalDate
    ): OutingApplicationHistoryResponse {
        val outings = outingService.getOutingHistoriesByStudentNameAndDate(
            studentName = studentName,
            date = date
        )

        return OutingApplicationHistoryResponse(outings)
    }
}
