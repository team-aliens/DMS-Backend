package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.QueryStudentPointHistoryResponse
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.user.service.GetUserService
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentPointHistoryUseCase(
    private val getUserService: GetUserService,
    private val queryStudentPort: QueryStudentPort,
    private val queryPointHistoryPort: QueryPointHistoryPort
) {

    fun execute(studentId: UUID, pageData: PageData): QueryStudentPointHistoryResponse {

        val user = getUserService.getCurrentUser()

        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException

        validateSameSchool(user.schoolId, student.schoolId)

        val pointHistories = queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
            studentName = student.name,
            gcn = student.gcn,
            pageData = pageData,
            isCancel = false
        )

        return QueryStudentPointHistoryResponse(pointHistories)
    }
}
