package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.auth.dto.CertifyEmailCodeRequest
import team.aliens.dms.domain.auth.exception.AuthCodeLimitNotFoundException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.CommandAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.QueryAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class CertifyEmailCodeUseCase(
    private val getUserService: GetUserService,
    private val queryAuthCodePort: QueryAuthCodePort,
    private val queryAuthCodeLimitPort: QueryAuthCodeLimitPort,
    private val commandAuthCodeLimitPort: CommandAuthCodeLimitPort
) {

    fun execute(request: CertifyEmailCodeRequest) {
        if (EmailType.PASSWORD == request.type) {
            getUserService.checkUserExistsByEmail(request.email)
        }

        val authCode = queryAuthCodePort.queryAuthCodeByEmailAndEmailType(request.email, request.type)
            ?: throw AuthCodeNotFoundException

        authCode.validateAuthCode(request.authCode)

        val authCodeLimit = queryAuthCodeLimitPort.queryAuthCodeLimitByEmailAndEmailType(request.email, request.type)
            ?: throw AuthCodeLimitNotFoundException

        commandAuthCodeLimitPort.saveAuthCodeLimit(
            authCodeLimit.certified()
        )
    }
}
