package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class UpdateStudentProfileUseCase(
    private val getUserService: GetUserService,
    private val commandStudentPort: CommandStudentPort
) {

    fun execute(profileImageUrl: String) {

        val student = getUserService.getCurrentStudent()

        commandStudentPort.saveStudent(
            student.copy(profileImageUrl = profileImageUrl)
        )
    }
}
