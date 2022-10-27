package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import team.aliens.dms.domain.manager.spi.ManagerQueryPointHistoryPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.global.annotation.ReadOnlyUseCase
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

        return GetStudentDetailsResponse(
            name = user.name,
            gcn = student.grade.toString().plus(student.classRoom).plus(student.number),
            profileImageUrl = user.profileImageUrl!!,
            bonusPoint = queryPointHistoryPort.getPointScore(studentId = studentId, isBonus = true),
            minusPoint = queryPointHistoryPort.getPointScore(studentId = studentId, isBonus = false),
            roomNumber = student.roomNumber,
            roomMates = roomMates
        )
    }
}