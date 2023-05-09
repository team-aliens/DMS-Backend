package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.student.dto.StudentDetailsResponse
import team.aliens.dms.domain.student.service.StudentService
import team.aliens.dms.domain.tag.spi.QueryTagPort
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentDetailsUseCase(
    private val pointService: PointService,
    private val studentService: StudentService,
    private val queryTagPort: QueryTagPort
) {

    fun execute(studentId: UUID): StudentDetailsResponse {

        val student = studentService.getStudentById(studentId)

        val (bonusPoint, minusPoint) =
            pointService.queryBonusAndMinusTotalPointByStudentGcnAndName(student.gcn, student.name)

        val roomMates = studentService.getRoommates(
            studentId = student.id,
            roomNumber = student.roomNumber,
            schoolId = student.schoolId
        )

        val tags = queryTagPort.queryTagsByStudentId(studentId)

        return StudentDetailsResponse.of(
            student = student,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            roomMates = roomMates,
            tags = tags
                .map { TagResponse.of(it) }
        )
    }
}
