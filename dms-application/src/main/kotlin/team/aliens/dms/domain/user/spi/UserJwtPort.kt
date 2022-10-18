package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.auth.dto.response.TokenResponse
import team.aliens.dms.domain.auth.model.Authority
import java.util.*

interface UserJwtPort {
    fun receiveToken(userId: UUID, authority: Authority): TokenResponse
}