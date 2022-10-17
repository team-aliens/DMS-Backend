package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.auth.dto.response.TokenResponse
import java.util.UUID

interface UserJwtPort {
    fun receiveToken(userId: UUID, authority: String): TokenResponse
}