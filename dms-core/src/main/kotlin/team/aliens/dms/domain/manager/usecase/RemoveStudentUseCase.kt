package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandRemainStatusPort
import team.aliens.dms.domain.student.spi.StudentCommandStudyRoomPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class RemoveStudentUseCase(
    private val securityPort: ManagerSecurityPort,
    private val queryUserPort: ManagerQueryUserPort,
    private val queryStudentPort: ManagerQueryStudentPort,
    private val commandRemainStatusPort: StudentCommandRemainStatusPort,
    private val commandStudyRoomPort: StudentCommandStudyRoomPort,
    private val commandStudentPort: CommandStudentPort,
    private val commandUserPort: ManagerCommandUserPort
) {

    fun execute(studentId: UUID) {
        val currentManagerId = securityPort.getCurrentUserId()

        val manager = queryUserPort.queryUserById(currentManagerId) ?: throw ManagerNotFoundException
        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException
        val studentUser = student.userId?.let { queryUserPort.queryUserById(it) } ?: throw UserNotFoundException

        validateSameSchool(student.schoolId, manager.schoolId)

        commandRemainStatusPort.deleteByStudentId(studentId)
        commandStudyRoomPort.deleteSeatApplicationByStudentId(studentId)

        commandStudentPort.saveStudent(
            student.copy(deletedAt = LocalDateTime.now())
        )

        commandUserPort.saveUser(
            studentUser.copy(deletedAt = LocalDateTime.now())
        )
    }
}
