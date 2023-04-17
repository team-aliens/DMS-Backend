package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.auth.dto.CertifyEmailRequest
import team.aliens.dms.domain.auth.exception.EmailMismatchException
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class CertifyEmailUseCase(
    private val userService: UserService
) {

    fun execute(request: CertifyEmailRequest) {
        val user = userService.queryUserByAccountId(request.accountId)

        if (user.email != request.email) {
            throw EmailMismatchException
        }
    }
}
