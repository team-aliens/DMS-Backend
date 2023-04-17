package team.aliens.dms.domain.student.usecase

import java.time.LocalDateTime
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.UserService

@UseCase
class StudentWithdrawalUseCase(
    private val userService: UserService,
    private val commandRemainStatusPort: CommandRemainStatusPort,
    private val commandStudyRoomPort: CommandStudyRoomPort,
    private val commandStudentPort: CommandStudentPort
) {

    fun execute() {

        val student = userService.getCurrentStudent()
        student.userId ?: throw UserNotFoundException

        commandRemainStatusPort.deleteByStudentId(student.id)
        commandStudyRoomPort.deleteSeatApplicationByStudentId(student.id)

        commandStudentPort.saveStudent(
            student.copy(deletedAt = LocalDateTime.now())
        )
        userService.deleteUserById(student.userId)
    }
}
