package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.outing.dto.OutingApplicationHistoriesResponse
import team.aliens.dms.domain.outing.service.OutingService
import java.time.LocalDate

@ReadOnlyUseCase
class GetOutingApplicationHistoriesUseCase(
    private val outingService: OutingService,
) {

    fun execute(studentName: String?, date: LocalDate): OutingApplicationHistoriesResponse {

        val outings = outingService.getOutingHistoriesByStudentNameAndDate(studentName = studentName, date = date)

        return OutingApplicationHistoriesResponse(outings)
    }
}
