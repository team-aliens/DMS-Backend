package team.aliens.dms.domain.auth.service.impl

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.auth.exception.AuthCodeLimitNotFoundException
import team.aliens.dms.domain.auth.exception.EmailAlreadyCertifiedException
import team.aliens.dms.domain.auth.exception.RefreshTokenNotFoundException
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.model.RefreshToken
import team.aliens.dms.domain.auth.service.GetAuthCodeService
import team.aliens.dms.domain.auth.spi.QueryAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.QueryRefreshTokenPort
import java.util.UUID

@Service
class GetAuthCodeServiceImpl(
    private val queryAuthCodeLimitPort: QueryAuthCodeLimitPort,
    private val queryRefreshTokenPort: QueryRefreshTokenPort
) : GetAuthCodeService {

    override fun getAuthCodeLimitByEmailAndEmailType(email: String, type: EmailType): AuthCodeLimit {
        val authCodeLimit = queryAuthCodeLimitPort.queryAuthCodeLimitByEmailAndEmailType(email, type)
            ?: throw AuthCodeLimitNotFoundException

        if (authCodeLimit.isVerified) {
            throw EmailAlreadyCertifiedException
        }

        return authCodeLimit
    }

    override fun getRefreshTokenByUserIdAndToken(userId: UUID, token: String): RefreshToken {
        return queryRefreshTokenPort.queryRefreshTokenByUserIdAndToken(userId, token)
            ?: throw RefreshTokenNotFoundException
    }
}
