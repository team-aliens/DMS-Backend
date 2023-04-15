package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.CommandUserService
import team.aliens.dms.domain.user.service.GetUserService
import java.time.LocalDateTime

@UseCase
class StudentWithdrawalUseCase(
    private val getUserService: GetUserService,
    private val commandUserService: CommandUserService,
    private val commandRemainStatusPort: CommandRemainStatusPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val commandStudentPort: CommandStudentPort
) {

    fun execute() {

        val student = getUserService.getCurrentStudent()
        student.userId ?: throw UserNotFoundException

        commandRemainStatusPort.deleteByStudentId(student.id)
        commandStudyRoomPort.deleteSeatApplicationByStudentId(student.id)

        commandStudentPort.saveStudent(
            student.copy(deletedAt = LocalDateTime.now())
        )
        commandUserService.deleteUserById(student.userId)
    }
}
