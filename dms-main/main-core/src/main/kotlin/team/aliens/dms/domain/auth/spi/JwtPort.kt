package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.model.Authority
import java.util.UUID

interface JwtPort {
    fun receiveToken(userId: UUID, authority: Authority, schoolId: UUID): TokenResponse
}
