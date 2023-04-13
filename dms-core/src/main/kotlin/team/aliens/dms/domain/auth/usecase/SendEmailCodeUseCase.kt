package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.auth.dto.SendEmailCodeRequest
import team.aliens.dms.domain.auth.exception.EmailAlreadyCertifiedException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.CommandAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.CommandAuthCodePort
import team.aliens.dms.domain.auth.spi.QueryAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@UseCase
class SendEmailCodeUseCase(
    private val sendEmailPort: SendEmailPort,
    private val queryUserPort: QueryUserPort,
    private val commandAuthCodePort: CommandAuthCodePort,
    private val queryAuthCodeLimitPort: QueryAuthCodeLimitPort,
    private val commandAuthCodeLimitPort: CommandAuthCodeLimitPort
) {

    fun execute(request: SendEmailCodeRequest) {
        if (EmailType.PASSWORD == request.type) {
            queryUserPort.queryUserByEmail(request.email) ?: throw UserNotFoundException
        }

        val authCodeLimit = queryAuthCodeLimitPort.queryAuthCodeLimitByEmailAndEmailType(request.email, request.type)
            ?: AuthCodeLimit(request.email, request.type)

        commandAuthCodeLimitPort.saveAuthCodeLimit(authCodeLimit.increaseCount())

        if (authCodeLimit.isVerified) {
            throw EmailAlreadyCertifiedException
        }

        val code = StringUtil.randomNumber(AuthCode.AUTH_CODE_SIZE)

        val authCode = AuthCode(
            code = code,
            email = request.email,
            type = request.type
        )
        commandAuthCodePort.saveAuthCode(authCode)

        sendEmailPort.sendAuthCode(request.email, request.type, code)
    }
}
