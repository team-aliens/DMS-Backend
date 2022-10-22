package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.spi.UserJwtPort
import java.util.*

interface JwtPort : UserJwtPort{

    fun receiveToken(userId: UUID, authority: Authority): TokenResponse
}