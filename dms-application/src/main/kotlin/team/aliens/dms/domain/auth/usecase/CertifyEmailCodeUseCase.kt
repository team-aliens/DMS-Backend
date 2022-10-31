package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.domain.auth.dto.CertifyEmailCodeRequest
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.auth.spi.CommandAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.common.annotation.UseCase

@UseCase
class CertifyEmailCodeUseCase(
    private val queryAuthCodePort: QueryAuthCodePort,
    private val queryUserPort: AuthQueryUserPort,
    private val commandAuthCodeLimitPort: CommandAuthCodeLimitPort
) {

    fun execute(request: CertifyEmailCodeRequest) {
        if (EmailType.PASSWORD == request.type) {
            queryUserPort.queryUserByEmail(request.email) ?: throw UserNotFoundException
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmailAndEmailType(request.email, request.type)
            ?: throw AuthCodeNotFoundException

        if (authCode.code != request.authCode) {
            throw AuthCodeMismatchException
        }

        commandAuthCodeLimitPort.saveAuthCodeLimit(
            AuthCodeLimit.certified(
                email = request.email,
                type = request.type
            )
        )
    }
}