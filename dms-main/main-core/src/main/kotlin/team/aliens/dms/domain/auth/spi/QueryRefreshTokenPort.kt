package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.RefreshToken
import java.util.UUID

interface QueryRefreshTokenPort {

    fun queryRefreshTokenByUserIdAndToken(userId: UUID, token: String): RefreshToken?
}
