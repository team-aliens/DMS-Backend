package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.RefreshToken
import java.util.UUID

interface CommandRefreshTokenPort {
    fun save(refreshToken: RefreshToken): RefreshToken

    /**
     * currentToken을 지우고 newRefreshToken을 저장한다. currentToken이 없으면(이미 쓰였거나 만료) null.
     */
    fun rotateRefreshToken(currentToken: String, newRefreshToken: RefreshToken): RefreshToken?

    fun deleteAllRefreshTokensByUserId(userId: UUID)
}
