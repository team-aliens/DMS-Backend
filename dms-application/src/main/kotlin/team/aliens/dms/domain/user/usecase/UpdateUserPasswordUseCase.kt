package team.aliens.dms.domain.user.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.exception.PasswordMismatchException
import team.aliens.dms.domain.user.dto.UpdateUserPasswordRequest
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.CommandUserPort
import team.aliens.dms.domain.user.spi.QueryUserPort
import team.aliens.dms.domain.user.spi.UserSecurityPort

@UseCase
class UpdateUserPasswordUseCase(
    private val securityPort: UserSecurityPort,
    private val queryUserPort: QueryUserPort,
    private val commandUserPort: CommandUserPort
) {

    fun execute(request: UpdateUserPasswordRequest) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        if (!securityPort.isPasswordMatch(request.oldPassword, user.password)) {
            throw PasswordMismatchException
        }

        val newEncodedPassword = securityPort.encodePassword(request.newPassword)

        commandUserPort.saveUser(
            user.copy(
                password = newEncodedPassword
            )
        )
    }
}