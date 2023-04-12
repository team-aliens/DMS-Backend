package team.aliens.dms.domain.user.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.auth.exception.PasswordMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import team.aliens.dms.domain.user.spi.UserSecurityPort

@ReadOnlyUseCase
class UserPasswordCompareUseCase(
    private val securityPort: UserSecurityPort,
    private val queryUserPort: QueryUserPort
) {

    fun execute(password: String) {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        if (!securityPort.isPasswordMatch(password, user.password)) {
            throw PasswordMismatchException
        }
    }
}
