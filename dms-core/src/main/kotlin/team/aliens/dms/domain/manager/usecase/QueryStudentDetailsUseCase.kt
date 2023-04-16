package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.tag.dto.TagResponse
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentDetailsUseCase(
    private val userService: UserService,
    private val queryStudentPort: QueryStudentPort,
    private val queryPointHistoryPort: QueryPointHistoryPort,
    private val queryTagPort: QueryTagPort
) {

    fun execute(studentId: UUID): GetStudentDetailsResponse {
        val user = userService.getCurrentUser()
        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException

        validateSameSchool(user.schoolId, student.schoolId)

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
                profileImageUrl = it.profileImageUrl
            )
        }

        val tags = queryTagPort.queryTagsByStudentId(studentId)
            .map {
                TagResponse(
                    id = it.id,
                    name = it.name,
                    color = it.color
                )
            }

        return GetStudentDetailsResponse(
            id = student.id,
            name = student.name,
            gcn = student.gcn,
            profileImageUrl = student.profileImageUrl,
            sex = student.sex,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            roomNumber = student.roomNumber,
            roomMates = roomMates,
            tags = tags
        )
    }
}
