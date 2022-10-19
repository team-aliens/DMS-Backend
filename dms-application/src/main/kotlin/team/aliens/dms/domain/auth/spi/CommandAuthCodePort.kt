package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.AuthCode

interface CommandAuthCodePort {
    fun saveAuthCode(authCode: AuthCode): AuthCode
}