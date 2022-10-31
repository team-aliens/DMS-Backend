package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import team.aliens.dms.domain.manager.spi.ManagerQueryPointHistoryPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@ReadOnlyUseCase
class GetStudentDetailsUseCase(
    private val queryUserPort: ManagerQueryUserPort,
    private val queryStudentPort: ManagerQueryStudentPort,
    private val queryPointHistoryPort: ManagerQueryPointHistoryPort
) {

    fun execute(studentId: UUID): GetStudentDetailsResponse {
        val user = queryUserPort.queryUserById(studentId) ?: throw UserNotFoundException
        val student = queryStudentPort.queryByUserId(studentId) ?: throw StudentNotFoundException

        val roomMateResponse = queryUserPort.queryUserByRoomNumberAndSchoolId(student.roomNumber, student.schoolId)
        val roomMates = roomMateResponse.map {
            GetStudentDetailsResponse.RoomMate(
                id = it.id,
                name = it.name,
                profileImageUrl = it.profileImageUrl!!
            )
        }

        val bonusPoint = queryPointHistoryPort.queryPointScore(studentId = studentId, isBonus = true)
        val minusPoint = queryPointHistoryPort.queryPointScore(studentId = studentId, isBonus = false)

        return GetStudentDetailsResponse(
            name = user.name,
            gcn = student.grade.toString().plus(student.classRoom).plus(student.number),
            profileImageUrl = user.profileImageUrl!!,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            roomNumber = student.roomNumber,
            roomMates = roomMates
        )
    }
}