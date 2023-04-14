package team.aliens.dms.domain.user.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.SecurityService
import team.aliens.dms.domain.user.dto.UpdateUserPasswordRequest
import team.aliens.dms.domain.user.service.CommandUserService
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class UpdateUserPasswordUseCase(
    private val getUserService: GetUserService,
    private val commandUserService: CommandUserService,
    private val securityService: SecurityService
) {

    fun execute(request: UpdateUserPasswordRequest) {
        val user = getUserService.getCurrentUser()

        securityService.checkIsPasswordMatches(request.currentPassword, user.password)

        val newEncodedPassword = securityService.encodePassword(request.newPassword)
        commandUserService.saveUser(
            user.copy(password = newEncodedPassword)
        )
    }
}
