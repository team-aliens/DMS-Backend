package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.point.dto.QueryStudentPointHistoryResponse
import team.aliens.dms.domain.point.spi.PointQueryManagerPort
import team.aliens.dms.domain.point.spi.PointQueryStudentPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentPointHistoryUseCase(
    private val securityPort: PointSecurityPort,
    private val queryManagerPort: PointQueryManagerPort,
    private val queryStudentPort: PointQueryStudentPort,
    private val queryPointHistoryPort: QueryPointHistoryPort
) {

    fun execute(studentId: UUID, pageData: PageData): QueryStudentPointHistoryResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentUserId) ?: throw ManagerNotFoundException

        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException
        if (student.schoolId != manager.schoolId) {
            throw SchoolMismatchException
        }

        val pointHistories = queryPointHistoryPort.queryPointHistoryByStudentGcnAndNameAndType(
            studentName = student.name,
            gcn = student.gcn,
            pageData = pageData
        )

        return QueryStudentPointHistoryResponse(pointHistories)
    }
}