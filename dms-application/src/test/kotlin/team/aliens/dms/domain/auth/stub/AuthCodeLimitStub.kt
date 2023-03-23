package team.aliens.dms.domain.auth.stub

import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import java.util.UUID

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
