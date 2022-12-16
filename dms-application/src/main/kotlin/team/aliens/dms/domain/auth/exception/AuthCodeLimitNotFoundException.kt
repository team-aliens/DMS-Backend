package team.aliens.dms.domain.auth.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.auth.error.AuthErrorCode

object AuthCodeLimitNotFoundException : DmsException(
    AuthErrorCode.AUTH_CODE_LIMIT_NOT_FOUND
)