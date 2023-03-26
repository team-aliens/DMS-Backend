package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerQueryPointHistoryPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryTagPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentDetailsUseCase(
    private val securityPort: ManagerSecurityPort,
    private val queryManagerPort: QueryManagerPort,
    private val queryStudentPort: ManagerQueryStudentPort,
    private val queryPointHistoryPort: ManagerQueryPointHistoryPort,
    private val queryTagPort: ManagerQueryTagPort
) {

    fun execute(studentId: UUID): GetStudentDetailsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentUserId) ?: throw ManagerNotFoundException

        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException

        validateSameSchool(manager.schoolId, student.schoolId)

        val (bonusPoint, minusPoint) =
            queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(student.gcn, student.name)

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

        val tags = queryTagPort.queryTagsByStudentId(studentId)
            .map {
                GetStudentDetailsResponse.TagResponse(
                    id = it.id,
                    name = it.name,
                    color = it.color
                )
            }

        return GetStudentDetailsResponse(
            name = student.name,
            gcn = student.gcn,
            profileImageUrl = student.profileImageUrl!!,
            sex = student.sex,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            roomNumber = student.roomNumber,
            roomMates = roomMates,
            tags = tags
        )
    }
}
