package team.aliens.dms.domain.point.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.point.dto.StudentPointHistoryResponse
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.student.service.StudentService

@ReadOnlyUseCase
class QueryStudentRecentPointHistoryUseCase(
    private val studentService: StudentService,
    private val pointService: PointService
) {

    fun execute(studentId: UUID): StudentPointHistoryResponse {

        val student = studentService.getStudentById(studentId)

        return pointService.queryCurrentPointHistoryBySchoolId(
            schoolId = student.schoolId,
            studentName = student.name,
            studentGcn = student.gcn
        )
    }
}
