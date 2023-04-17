package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.user.service.UserService

@UseCase
class UpdateStudentProfileUseCase(
    private val userService: UserService,
    private val commandStudentPort: CommandStudentPort
) {

    fun execute(profileImageUrl: String) {

        val student = userService.getCurrentStudent()

        commandStudentPort.saveStudent(
            student.copy(profileImageUrl = profileImageUrl)
        )
    }
}
