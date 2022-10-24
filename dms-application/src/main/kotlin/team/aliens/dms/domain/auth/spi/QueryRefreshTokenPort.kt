package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.RefreshToken

interface QueryRefreshTokenPort {
    fun queryRefreshTokenByToken(token: String): RefreshToken?
}