package team.aliens.dms.domain.user.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.SecurityService
import team.aliens.dms.domain.user.dto.UpdateUserPasswordRequest
import team.aliens.dms.domain.user.service.UserService

@UseCase
class UpdateUserPasswordUseCase(
    private val userService: UserService,
    private val securityService: SecurityService
) {

    fun execute(request: UpdateUserPasswordRequest) {
        val user = userService.getCurrentUser()

        securityService.checkIsPasswordMatches(request.currentPassword, user.password)

        val newEncodedPassword = securityService.encodePassword(request.newPassword)
        userService.saveUser(
            user.copy(password = newEncodedPassword)
        )
    }
}
