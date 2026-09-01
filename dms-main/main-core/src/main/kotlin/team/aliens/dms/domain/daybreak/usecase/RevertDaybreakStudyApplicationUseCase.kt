package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.request.RevertDaybreakStudyApplicationRequest
import team.aliens.dms.domain.daybreak.exception.DaybreakStudentSchoolMismatchException
import team.aliens.dms.domain.daybreak.service.DaybreakService

@UseCase
class RevertDaybreakStudyApplicationUseCase(
    private val daybreakService: DaybreakService,
    private val securityService: SecurityService
) {

    fun execute(request: RevertDaybreakStudyApplicationRequest) {

        val currentSchoolId = securityService.getCurrentSchoolId()

        val applications = daybreakService.getAllByIdIn(request.applicationIdList)

        applications.forEach {
            if (it.schoolId != currentSchoolId) throw DaybreakStudentSchoolMismatchException
-            it.revert()
        }

        daybreakService.saveAllDaybreakStudyApplications(applications)
    }
}
