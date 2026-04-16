package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.response.GeneralTeacherDaybreakStudyApplicationsResponse
import team.aliens.dms.domain.daybreak.service.DaybreakService
import java.time.LocalDate
import java.util.UUID

@UseCase
class QueryGeneralTeacherDaybreakStudyApplicationUseCase(
    private val daybreakService: DaybreakService,
    private val securityService: SecurityService
) {

    fun execute(typeId: UUID?, date: LocalDate, pageData: PageData): GeneralTeacherDaybreakStudyApplicationsResponse {

        val teacherId = securityService.getCurrentUserId()

        return GeneralTeacherDaybreakStudyApplicationsResponse(
            daybreakService.generalTeacherGetDaybreakStudyApplications(teacherId, typeId, date, pageData)
        )
    }
}
