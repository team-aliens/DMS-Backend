package team.aliens.dms.domain.user.spi

import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.model.Authority
import java.util.UUID

interface UserJwtPort {

    fun receiveToken(userId: UUID, authority: Authority): TokenResponse

}