package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.dto.response.HeadTeacherDaybreakStudyApplicationsResponse
import team.aliens.dms.domain.daybreak.model.Status
import team.aliens.dms.domain.daybreak.service.DaybreakService
import java.util.UUID

@UseCase
class QueryHeadTeacherDaybreakStudyApplicationUseCase (
    private val daybreakService: DaybreakService
){

    fun execute(typeId: UUID?, status: Status?, pageData: PageData): HeadTeacherDaybreakStudyApplicationsResponse {
        return HeadTeacherDaybreakStudyApplicationsResponse(daybreakService.headTeacherGetDaybreakStudyApplications(typeId, status, pageData))
    }
}