package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.RefreshToken

interface CommandRefreshTokenPort {
    fun save(refreshToken: RefreshToken): RefreshToken
}
