package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.StudentCommandRemainStatusPort
import team.aliens.dms.domain.student.spi.StudentCommandStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentQueryStudyRoomPort
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class RemoveStudentUseCase(
    private val securityPort: ManagerSecurityPort,
    private val queryUserPort: ManagerQueryUserPort,
    private val queryStudentPort: ManagerQueryStudentPort,
    private val commandRemainStatusPort: StudentCommandRemainStatusPort,
    private val queryStudyRoomPort: StudentQueryStudyRoomPort,
    private val commandStudyRoomPort: StudentCommandStudyRoomPort,
    private val commandUserPort: ManagerCommandUserPort
) {

    fun execute(studentId: UUID) {
        val currentManagerId = securityPort.getCurrentUserId()

        val manager = queryUserPort.queryUserById(currentManagerId) ?: throw ManagerNotFoundException
        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException
        val studentUser = queryUserPort.queryUserById(studentId) ?: throw UserNotFoundException

        validateSameSchool(student.schoolId, manager.schoolId)

        // 잔류 내역 삭제
        commandRemainStatusPort.deleteByStudentId(studentId)

        // 자습실 신청 상태 제거
        queryStudyRoomPort.querySeatByStudentId(studentId)?.let { seat ->
            val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException
            commandStudyRoomPort.saveSeat(
                seat.unUse()
            )
            commandStudyRoomPort.saveStudyRoom(
                studyRoom.unApply()
            )
        }

        // 잔류 내역 삭제
        commandRemainStatusPort.deleteByStudentId(studentId)

        // 자습실 신청 상태 제거
        queryStudyRoomPort.querySeatByStudentId(studentId)?.let { seat ->
            val studyRoom = queryStudyRoomPort.queryStudyRoomById(seat.studyRoomId) ?: throw StudyRoomNotFoundException
            commandStudyRoomPort.saveSeat(
                seat.unUse()
            )
            commandStudyRoomPort.saveStudyRoom(
                studyRoom.unApply()
            )
        }

        commandUserPort.saveUser(
            studentUser.copy(deletedAt = LocalDateTime.now())
        )
    }
}
