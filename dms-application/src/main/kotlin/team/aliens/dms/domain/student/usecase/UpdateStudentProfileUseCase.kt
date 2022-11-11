package team.aliens.dms.domain.student.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User

@UseCase
class UpdateStudentProfileUseCase(
    private val securityPort: StudentSecurityPort,
    private val queryUserPort: StudentQueryUserPort,
    private val commandUserPort: StudentCommandUserPort
) {

    fun execute(profileImageUrl: String?) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        commandUserPort.saveUser(
            user.copy(profileImageUrl = profileImageUrl ?: User.PROFILE_IMAGE)
        )
    }
}