package team.aliens.dms.domain.manager.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerCommandStudentPort
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class RemoveStudentUseCase(
    private val securityPort: ManagerSecurityPort,
    private val queryUserPort: ManagerQueryUserPort,
    private val queryStudentPort: ManagerQueryStudentPort,
    private val commandStudentPort: ManagerCommandStudentPort,
    private val commandUserPort: ManagerCommandUserPort
) {

    fun execute(studentId: UUID) {
        val managerId = securityPort.getCurrentUserId()

        val manager = queryUserPort.queryUserById(managerId) ?: throw ManagerNotFoundException
        val student = queryStudentPort.queryStudentById(studentId) ?: throw StudentNotFoundException
        val studentUser = queryUserPort.queryUserById(student.studentId) ?: throw UserNotFoundException

        if (student.schoolId != manager.schoolId) {
            throw SchoolMismatchException
        }

        commandStudentPort.deleteStudent(student)

        commandUserPort.saveUser(
            studentUser.copy(deletedAt = LocalDateTime.now())
        )
    }
}