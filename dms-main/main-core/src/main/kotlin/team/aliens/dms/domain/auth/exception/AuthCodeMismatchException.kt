package team.aliens.dms.domain.auth.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.auth.exception.error.AuthErrorCode

object AuthCodeMismatchException : DmsException(
    AuthErrorCode.AUTH_CODE_MISMATCH
)
