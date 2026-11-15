package team.aliens.dms.domain.auth.service

import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.model.RefreshToken
import java.util.UUID

interface GetAuthCodeService {
    fun getAuthCodeLimitByEmailAndEmailType(email: String, type: EmailType): AuthCodeLimit

    fun getRefreshTokenByUserIdAndToken(userId: UUID, token: String): RefreshToken
}
