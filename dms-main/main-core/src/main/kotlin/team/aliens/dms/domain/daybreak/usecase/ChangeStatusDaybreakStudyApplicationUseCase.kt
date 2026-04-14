package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.request.ChangeDaybreakStudyApplicationStatusRequest
import team.aliens.dms.domain.daybreak.service.DaybreakService

@UseCase
class ChangeStatusDaybreakStudyApplicationUseCase(
    private val daybreakService: DaybreakService,
    private val securityService: SecurityService
) {

    fun execute(request : ChangeDaybreakStudyApplicationStatusRequest) {

        val authority = securityService.getCurrentUserAuthority()

        val application = daybreakService.getAllByIdIn(request.applicationIds)

        application.forEach { it.changeStatus(authority, request.status) }

        daybreakService.saveAllDaybreakStudyApplications(application)
    }
}