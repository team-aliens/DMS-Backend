package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerQueryPointPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentDetailsUseCase(
    private val securityPort: ManagerSecurityPort,
    private val queryManagerPort: QueryManagerPort,
    private val queryStudentPort: ManagerQueryStudentPort,
    private val queryPointPort: ManagerQueryPointPort
) {

    fun execute(studentId: UUID): GetStudentDetailsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentUserId) ?: throw ManagerNotFoundException

        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException

        if (manager.schoolId != student.schoolId) {
            throw SchoolMismatchException
        }

        val (bonusPoint, minusPoint) =
            queryPointPort.queryBonusAndMinusTotalPointByGcnAndStudentName(student.gcn, student.name)

        val roomMates = queryStudentPort.queryUserByRoomNumberAndSchoolId(
            roomNumber = student.roomNumber,
            schoolId = student.schoolId
        ).filter {
            it.id != studentId
        }.map {
            GetStudentDetailsResponse.RoomMate(
                id = it.id,
                name = it.name,
                profileImageUrl = it.profileImageUrl!!
            )
        }

        return GetStudentDetailsResponse(
            name = student.name,
            gcn = student.gcn,
            profileImageUrl = student.profileImageUrl!!,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            roomNumber = student.roomNumber,
            roomMates = roomMates
        )
    }
}