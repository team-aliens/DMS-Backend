package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.CommandUserPort
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.time.LocalDateTime

@UseCase
class StudentWithdrawalUseCase(
    private val securityPort: SecurityPort,
    private val queryStudentPort: QueryStudentPort,
    private val queryUserPort: QueryUserPort,
    private val commandRemainStatusPort: CommandRemainStatusPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val commandStudentPort: CommandStudentPort,
    private val commandUserPort: CommandUserPort
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
