package team.aliens.dms.domain.auth.service

import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType

interface CommandAuthCodeService {
    fun saveAuthCode(authCode: AuthCode)

    fun saveAuthCodeLimit(authCodeLimit: AuthCodeLimit)

    fun saveIncreasedAuthCodeLimitByEmailAndType(email: String, type: EmailType)
}
