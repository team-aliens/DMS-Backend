package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.QueryStudentPointHistoryResponse
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentPointHistoryUseCase(
    private val userService: UserService,
    private val studentService: StudentService,
    private val pointService: PointService
) {

    fun execute(studentId: UUID, pageData: PageData): QueryStudentPointHistoryResponse {

        val user = userService.getCurrentUser()
        val student = studentService.getStudentById(studentId, user.schoolId)

        val pointHistories = pointService.queryPointHistoryByStudentGcnAndNameAndType(
            studentName = student.name,
            gcn = student.gcn,
            pageData = pageData,
            isCancel = false
        )

        return QueryStudentPointHistoryResponse(pointHistories)
    }
}
