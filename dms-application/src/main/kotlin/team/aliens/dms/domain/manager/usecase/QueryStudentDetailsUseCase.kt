package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import team.aliens.dms.domain.manager.spi.ManagerQueryPointPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import java.util.UUID

@ReadOnlyUseCase
class QueryStudentDetailsUseCase(
    private val queryUserPort: ManagerQueryUserPort,
    private val queryStudentPort: ManagerQueryStudentPort,
    private val queryPointPort: ManagerQueryPointPort
) {

    fun execute(studentId: UUID): GetStudentDetailsResponse {
        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException

        val bonusPoint = queryPointPort.queryTotalBonusPoint(studentId)
        val minusPoint = queryPointPort.queryTotalMinusPoint(studentId)

        val roomMateResponse = queryUserPort.queryUserByRoomIdAndSchoolId(
            roomId = student.roomNumber,
            schoolId = student.schoolId
        )
        val roomMates = roomMateResponse.map {
            GetStudentDetailsResponse.RoomMate(
                id = it.id,
                name = it.name,
                profileImageUrl = it.profileImageUrl ?: Student.PROFILE_IMAGE
            )
        }

        return GetStudentDetailsResponse(
            name = student.name,
            gcn = student.gcn,
            profileImageUrl = student.profileImageUrl ?: Student.PROFILE_IMAGE,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            roomNumber = student.roomNumber,
            roomMates = roomMates
        )
    }
}