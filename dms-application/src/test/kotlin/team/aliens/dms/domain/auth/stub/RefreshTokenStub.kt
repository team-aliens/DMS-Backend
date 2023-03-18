package team.aliens.dms.domain.auth.stub

import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.RefreshToken
import java.util.UUID

internal fun createRefreshToken(
    token: String = "rlkemagkldamgl",
    userId: UUID = UUID.randomUUID(),
    authority: Authority = Authority.MANAGER,
    expirationTime: Int = 1800
) = RefreshToken(
    token = token,
    userId = userId,
    authority = authority,
    expirationTime = expirationTime
)