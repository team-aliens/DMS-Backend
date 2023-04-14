package team.aliens.dms.domain.student.usecase

import java.time.LocalDateTime
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.GetUserService
import team.aliens.dms.domain.user.spi.CommandUserPort
import team.aliens.dms.domain.user.spi.QueryUserPort

@UseCase
class StudentWithdrawalUseCase(
    private val getUserService: GetUserService,
    private val queryUserPort: QueryUserPort,
    private val commandRemainStatusPort: CommandRemainStatusPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val commandStudentPort: CommandStudentPort,
    private val commandUserPort: CommandUserPort
) {

    fun execute() {

        val student = getUserService.getCurrentStudent()
        val studentUser = student.userId?.let { queryUserPort.queryUserById(it) } ?: throw UserNotFoundException

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
