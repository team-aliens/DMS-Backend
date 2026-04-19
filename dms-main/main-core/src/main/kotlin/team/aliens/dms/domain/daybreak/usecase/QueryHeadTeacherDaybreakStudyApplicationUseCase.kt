package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.response.DaybreakStudyApplicationResponse
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.teacher.service.TeacherService
import java.time.LocalDate
import java.util.UUID

@UseCase
class QueryHeadTeacherDaybreakStudyApplicationUseCase(
    private val daybreakService: DaybreakService,
    private val securityService: SecurityService,
    private val teacherService: TeacherService
) {

    fun execute(typeId: UUID?, date: LocalDate, status: Status?, pageData: PageData): DaybreakStudyApplicationResponse {

        val teacher = teacherService.getTeacherById(securityService.getCurrentUserId())

        return DaybreakStudyApplicationResponse(
            daybreakService.headTeacherGetDaybreakStudyApplications(teacher.grade, typeId, date, status, pageData)
        )
    }
}
