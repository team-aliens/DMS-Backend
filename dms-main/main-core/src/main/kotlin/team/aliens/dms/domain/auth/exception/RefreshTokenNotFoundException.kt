package team.aliens.dms.domain.auth.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.auth.exception.error.AuthErrorCode

object RefreshTokenNotFoundException : DmsException(
    AuthErrorCode.REFRESH_TOKEN_NOT_FOUND
)
