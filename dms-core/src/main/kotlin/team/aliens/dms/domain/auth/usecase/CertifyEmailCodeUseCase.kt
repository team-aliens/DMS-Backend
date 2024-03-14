package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.dto.CertifyEmailCodeRequest
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.service.AuthService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class CertifyEmailCodeUseCase(
    private val userService: UserService,
    private val authService: AuthService
) {

    fun execute(request: CertifyEmailCodeRequest) {
        if (EmailType.PASSWORD == request.type) {
            userService.checkUserExistsByEmail(request.email)
        }

        authService.checkAuthCodeExists(request.authCode)

        val authCodeLimit = authService.getAuthCodeLimitByEmailAndEmailType(request.email, request.type)

        authService.saveAuthCodeLimit(
            authCodeLimit.certified()
        )
    }
}
