package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.request.CreateDaybreakStudyTypeRequest
import team.aliens.dms.domain.daybreak.model.DaybreakStudyType
import team.aliens.dms.domain.daybreak.service.DaybreakService

@UseCase
class CreateDaybreakStudyTypeUseCase(
    private val daybreakService: DaybreakService,
    private val securityService: SecurityService
) {

    fun execute(request: CreateDaybreakStudyTypeRequest) {

        val schoolId = securityService.getCurrentSchoolId()

        daybreakService.checkDaybreakStudyTypeExists(schoolId, request.name)

        daybreakService.saveDaybreakStudyType(
            DaybreakStudyType(
                name = request.name,
                schoolId = schoolId
            )
        )
    }
}
