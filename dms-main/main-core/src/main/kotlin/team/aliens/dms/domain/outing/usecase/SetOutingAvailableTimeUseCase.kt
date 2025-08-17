package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.outing.dto.request.SetOutingAvailableTimeRequest
import team.aliens.dms.domain.outing.dto.response.SetOutingAvailableTimeResponse
import team.aliens.dms.domain.outing.model.OutingAvailableTime
import team.aliens.dms.domain.outing.service.OutingService

@UseCase
class SetOutingAvailableTimeUseCase(
    private val outingService: OutingService,
    private val securityService: SecurityService,
) {

    fun execute(request: SetOutingAvailableTimeRequest): SetOutingAvailableTimeResponse {
        outingService.checkOutingAvailableTimeOverlap(
            dayOfWeek = request.dayOfWeek,
            outingTime = request.outingTime,
            arrivalTime = request.arrivalTime
        )

        val outingTime = outingService.saveOutingAvailableTime(
            OutingAvailableTime(
                schoolId = securityService.getCurrentSchoolId(),
                dayOfWeek = request.dayOfWeek,
                outingTime = request.outingTime,
                arrivalTime = request.arrivalTime,
                enabled = true
            )
        )

        return SetOutingAvailableTimeResponse(outingTime.id)
    }
}
