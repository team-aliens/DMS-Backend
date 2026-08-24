package team.aliens.dms.domain.daybreak.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.daybreak.dto.response.DaybreakStudyApplicationResponse
import team.aliens.dms.domain.daybreak.exception.DaybreakStudentSchoolMismatchException
import team.aliens.dms.domain.daybreak.service.DaybreakService
import team.aliens.dms.domain.student.service.StudentService
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentDaybreakStudyApplicationHistoryUseCase(
    private val daybreakService: DaybreakService,
    private val studentService: StudentService,
    private val securityService: SecurityService
) {

    fun execute(studentId: UUID, pageData: PageData): DaybreakStudyApplicationResponse {

        val student = studentService.getStudentById(studentId)

        if (student.schoolId != securityService.getCurrentSchoolId()) {
            throw DaybreakStudentSchoolMismatchException
        }

        return DaybreakStudyApplicationResponse(
            daybreakService.getStudentDaybreakStudyApplicationHistoryByStudentId(studentId, pageData)
        )
    }
}
