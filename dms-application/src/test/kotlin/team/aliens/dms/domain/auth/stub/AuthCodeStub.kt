package team.aliens.dms.domain.auth.stub

import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.EmailType

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