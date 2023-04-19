package team.aliens.dms.domain.auth.service

import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.model.RefreshToken

interface GetAuthCodeService {
    fun getAuthCodeLimitByEmailAndEmailType(email: String, type: EmailType): AuthCodeLimit

    fun getRefreshTokenByToken(token: String): RefreshToken
}
