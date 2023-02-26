package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.AuthCodeLimit

interface CommandAuthCodeLimitPort {

    fun saveAuthCodeLimit(authCodeLimit: AuthCodeLimit): AuthCodeLimit
}
