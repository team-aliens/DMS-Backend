package team.aliens.dms.domain.auth.service.impl

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.exception.UnverifiedAuthCodeException
import team.aliens.dms.domain.auth.service.CheckAuthCodeService
import team.aliens.dms.domain.auth.spi.QueryAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort

@Service
class CheckAuthCodeServiceImpl(
    private val queryAuthCodePort: QueryAuthCodePort,
    private val queryAuthCodeLimitPort: QueryAuthCodeLimitPort
) : CheckAuthCodeService {

    override fun checkAuthCodeExists(code: String) {
        if (!queryAuthCodePort.existsAuthCodeByCode(code)) {
            throw AuthCodeMismatchException
        }
    }

    override fun checkAuthCodeByEmail(email: String, code: String) {
        (
            queryAuthCodePort.queryAuthCodeByEmail(email)
                ?: throw AuthCodeNotFoundException
            )
            .apply { validateAuthCode(code) }
    }

    override fun checkAuthCodeLimitIsVerifiedByEmail(email: String) {
        val authCodeLimit = queryAuthCodeLimitPort.queryAuthCodeLimitByEmail(email)
            ?: throw AuthCodeNotFoundException

        if (!authCodeLimit.isVerified) {
            throw UnverifiedAuthCodeException
        }
    }
}
