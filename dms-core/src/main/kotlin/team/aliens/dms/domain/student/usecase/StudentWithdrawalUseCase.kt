package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandRemainStatusPort
import team.aliens.dms.domain.student.spi.StudentCommandStudyRoomPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalDateTime

@UseCase
class StudentWithdrawalUseCase(
    private val securityPort: StudentSecurityPort,
    private val queryStudentPort: QueryStudentPort,
    private val queryUserPort: StudentQueryUserPort,
    private val commandRemainStatusPort: StudentCommandRemainStatusPort,
    private val commandStudyRoomPort: StudentCommandStudyRoomPort,
    private val commandStudentPort: CommandStudentPort,
    private val commandUserPort: StudentCommandUserPort
) {

    fun execute() {
        val currentUserId = securityPort.getCurrentUserId()
        val student = queryStudentPort.queryStudentByUserId(currentUserId) ?: throw StudentNotFoundException
        val studentUser = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        commandRemainStatusPort.deleteByStudentId(student.id)
        commandStudyRoomPort.deleteSeatApplicationByStudentId(student.id)

        commandStudentPort.saveStudent(
            student.copy(deletedAt = LocalDateTime.now())
        )

        commandUserPort.saveUser(
            studentUser.copy(deletedAt = LocalDateTime.now())
        )
    }
}
