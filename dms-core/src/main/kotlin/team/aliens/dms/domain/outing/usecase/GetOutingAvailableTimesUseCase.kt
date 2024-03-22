package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.outing.dto.OutingAvailableTimesResponse
import team.aliens.dms.domain.outing.service.OutingService
import java.time.DayOfWeek

@ReadOnlyUseCase
class GetOutingAvailableTimesUseCase(
    private val outingService: OutingService
) {

    fun execute(dayOfWeek: DayOfWeek): OutingAvailableTimesResponse {
        val outingAvailableTimes = outingService.getOutingAvailableTimesByDayOfWeek(dayOfWeek)

        return OutingAvailableTimesResponse(outingAvailableTimes)
    }
}
