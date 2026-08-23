package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.daybreak.dto.response.DaybreakStudyApplicationResponse
import team.aliens.dms.domain.daybreak.service.DaybreakService
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentDaybreakStudyApplicationHistoryUseCase(
    private val daybreakService: DaybreakService
) {

    fun execute(studentId: UUID): DaybreakStudyApplicationResponse {

        return DaybreakStudyApplicationResponse(
            daybreakService.getStudentDaybreakStudyApplicationHistoryByStudentId(studentId)
        )
    }
}
