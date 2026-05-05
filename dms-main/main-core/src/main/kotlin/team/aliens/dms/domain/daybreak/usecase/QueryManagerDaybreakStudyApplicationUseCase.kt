package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.daybreak.dto.response.DaybreakStudyApplicationResponse
import team.aliens.dms.domain.daybreak.service.DaybreakService

@ReadOnlyUseCase
class QueryManagerDaybreakStudyApplicationUseCase(
    private val daybreakService: DaybreakService,
) {
    fun execute(grade: Int?, pageData: PageData): DaybreakStudyApplicationResponse {
        return DaybreakStudyApplicationResponse(
            daybreakService.managerGetDaybreakStudyApplications(grade, pageData)
        )
    }
}
