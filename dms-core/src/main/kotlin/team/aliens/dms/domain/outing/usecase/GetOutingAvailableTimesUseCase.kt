package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.outing.dto.response.OutingAvailableTimesResponse
import team.aliens.dms.domain.outing.service.OutingService
import java.time.DayOfWeek

@ReadOnlyUseCase
class GetOutingAvailableTimesUseCase(
    private val outingService: OutingService,
    private val securityService: SecurityService,
) {

    fun execute(dayOfWeek: DayOfWeek): OutingAvailableTimesResponse {
        val schoolId = securityService.getCurrentSchoolId()

        val outingAvailableTimes = outingService.getOutingAvailableTimesByDayOfWeek(
            dayOfWeek = dayOfWeek,
            schoolId = schoolId
        )

        return OutingAvailableTimesResponse(outingAvailableTimes)
    }
}
