package team.aliens.dms.domain.point.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.point.dto.QueryStudentPointHistoryResponse
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.student.service.StudentService

@ReadOnlyUseCase
class QueryStudentPointHistoryUseCase(
    private val studentService: StudentService,
    private val pointService: PointService
) {

    fun execute(studentId: UUID, pageData: PageData): QueryStudentPointHistoryResponse {

        val student = studentService.getStudentById(studentId)

        val pointHistories = pointService.queryPointHistoryByStudentGcnAndNameAndType(
            studentName = student.name,
            gcn = student.gcn,
            pageData = pageData,
            isCancel = false
        )

        return QueryStudentPointHistoryResponse(pointHistories)
    }
}
