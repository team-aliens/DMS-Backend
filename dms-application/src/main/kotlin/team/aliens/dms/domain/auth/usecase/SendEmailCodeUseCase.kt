package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.domain.auth.dto.SendEmailCodeRequest
import team.aliens.dms.domain.auth.exception.EmailAlreadyCertifiedException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.spi.*
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.global.annotation.UseCase
import team.aliens.dms.global.spi.GenerateRandomNumberStringPort

@UseCase
class SendEmailCodeUseCase(
    private val sendEmailPort: SendEmailPort,
    private val queryUserPort: AuthQueryUserPort,
    private val commandAuthCodePort: CommandAuthCodePort,
    private val getEmailCodePort: GenerateRandomNumberStringPort,
    private val queryAuthCodeLimitPort: QueryAuthCodeLimitPort,
    private val commandAuthCodeLimitPort: CommandAuthCodeLimitPort
) {

    fun execute(request: SendEmailCodeRequest) {

        val user = queryUserPort.queryUserByEmail(request.email) ?: throw UserNotFoundException

        val authCodeLimit =
            queryAuthCodeLimitPort.queryAuthCodeLimitByUserIdAndType(user.id, request.type)
                ?: AuthCodeLimit(user.id, request.type)

        if (authCodeLimit.isVerified) {
            throw EmailAlreadyCertifiedException
        }

        commandAuthCodeLimitPort.saveAuthCodeLimit(authCodeLimit.increaseCount())

        val code = getEmailCodePort.getRandomNumberString(6);

        val authCode = AuthCode(
            code = code,
            userId = user.id,
            type = request.type
        )
        commandAuthCodePort.saveAuthCode(authCode)

        sendEmailPort.sendEmailCode(request.email, request.type, code)
    }

}