package team.aliens.dms.domain.auth.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.auth.error.AuthErrorCode

object AuthCodeNotFoundException : DmsException(
    AuthErrorCode.AUTH_CODE_NOT_FOUND
)
