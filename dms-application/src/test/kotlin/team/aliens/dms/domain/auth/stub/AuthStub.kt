package team.aliens.dms.domain.auth.stub

import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.model.RefreshToken
import java.util.UUID

internal fun createAuthCodeStub(
    code: String = "123546",
    email: String = "email@dsm.hs.kr",
    type: EmailType = EmailType.SIGNUP,
    expirationTime: Int = 0
) = AuthCode(
    code = code,
    email = email,
    type = type,
    expirationTime = expirationTime
)

internal fun createAuthCodeLimitStub(
    id: UUID = UUID.randomUUID(),
    email: String = "email@dsm.hs.kr",
    type: EmailType = EmailType.SIGNUP,
    attemptCount: Int = 0,
    isVerified: Boolean = false,
    expirationTime: Int = AuthCodeLimit.EXPIRED
) = AuthCodeLimit(
    id = id,
    email = email,
    type = type,
    attemptCount = attemptCount,
    isVerified = isVerified,
    expirationTime = expirationTime
)

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
