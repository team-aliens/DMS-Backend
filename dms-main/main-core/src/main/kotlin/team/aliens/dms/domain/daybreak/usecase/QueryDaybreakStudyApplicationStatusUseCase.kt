package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.daybreak.dto.response.DaybreakStudyApplicationStatusResponse
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.student.service.StudentService

@ReadOnlyUseCase
class QueryDaybreakStudyApplicationStatusUseCase(
    private val daybreakService: DaybreakService,
    private val studentService: StudentService
) {

    fun execute() : DaybreakStudyApplicationStatusResponse{
        val student = studentService.getCurrentStudent()

        val statusInfo = daybreakService.getRecentDaybreakStudyApplicationStatusByStudentId(student.id)

        return DaybreakStudyApplicationStatusResponse(
            status = statusInfo.status,
            startDate = statusInfo.startDate,
            endDate = statusInfo.endDate
        )
    }
}