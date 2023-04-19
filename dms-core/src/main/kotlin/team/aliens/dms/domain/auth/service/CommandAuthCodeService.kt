package team.aliens.dms.domain.auth.service

import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.AuthCodeLimit

interface CommandAuthCodeService {
    fun saveAuthCode(authCode: AuthCode)

    fun saveAuthCodeLimit(authCodeLimit: AuthCodeLimit)
}
