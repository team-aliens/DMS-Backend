/*package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.response.GeneralTeacherDaybreakStudyApplicationsResponse
import team.aliens.dms.domain.daybreak.dto.response.HeadTeacherDaybreakStudyApplicationsResponse
import team.aliens.dms.domain.daybreak.dto.response.TeacherDaybreakStudyApplicationResponse
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.teacher.service.TeacherService
import java.util.UUID

@UseCase
class QueryTeacherDaybreakStudyApplicationUseCase(
    private val daybreakService: DaybreakService,
    private val teacherService: TeacherService,
    private val securityService: SecurityService
) {

    fun execute(typeId: UUID?, status: Status?, pageData: PageData): TeacherDaybreakStudyApplicationResponse {

        val role = teacherService.getTeacherRoleById(securityService.getCurrentUserId())

        return when (role) {
            Role.GENERAL_TEACHER -> {
                GeneralTeacherDaybreakStudyApplicationsResponse(daybreakService.generalTeacherGetDaybreakStudyApplications(typeId, pageData))
            }

            Role.HEAD_TEACHER -> {
                HeadTeacherDaybreakStudyApplicationsResponse(daybreakService.headTeacherGetDaybreakStudyApplications(typeId, status, pageData))
            }
        }
    }
}
*/