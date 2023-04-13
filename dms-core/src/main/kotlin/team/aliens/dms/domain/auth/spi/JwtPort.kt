package team.aliens.dms.domain.auth.spi

import java.util.UUID
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.model.Authority

interface JwtPort {
    fun receiveToken(userId: UUID, authority: Authority): TokenResponse
}
