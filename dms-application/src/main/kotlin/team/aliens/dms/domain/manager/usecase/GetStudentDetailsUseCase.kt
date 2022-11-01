package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.GCNToStringPort
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
    private val queryPointHistoryPort: ManagerQueryPointHistoryPort,
    private val gcnToStringPort: GCNToStringPort
) {

    fun execute(studentId: UUID): GetStudentDetailsResponse {
        val user = queryUserPort.queryUserById(studentId) ?: throw UserNotFoundException
        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException

        val gcn = gcnToStringPort.gcnToString(student.grade, student.classRoom, student.number)

        val bonusPoint = queryPointHistoryPort.queryTotalBonusPoint(studentId = studentId)
        val minusPoint = queryPointHistoryPort.queryTotalMinusPoint(studentId = studentId)

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
            gcn = gcn,
            profileImageUrl = user.profileImageUrl!!,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            roomNumber = student.roomNumber,
            roomMates = roomMates
        )
    }
}