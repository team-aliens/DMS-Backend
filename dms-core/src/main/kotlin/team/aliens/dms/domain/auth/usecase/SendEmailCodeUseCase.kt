package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.auth.dto.SendEmailCodeRequest
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.service.AuthService
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.domain.user.service.UserService

@UseCase
class SendEmailCodeUseCase(
    private val sendEmailPort: SendEmailPort,
    private val userService: UserService,
    private val authService: AuthService,
) {

    fun execute(request: SendEmailCodeRequest) {
        if (EmailType.PASSWORD == request.type) {
            userService.checkUserExistsByEmail(request.email)
        }

        authService.saveIncreasedAuthCodeLimitByEmailAndType(request.email, request.type)

        val code = StringUtil.randomNumber(AuthCode.AUTH_CODE_SIZE)
        authService.saveAuthCode(
            AuthCode(
                code = code,
                email = request.email,
                type = request.type
            )
        )

        sendEmailPort.sendAuthCode(request.email, request.type, code)
    }
}
