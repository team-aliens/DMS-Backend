package team.aliens.dms.domain.auth.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.auth.error.AuthErrorCode

object PasswordMismatchException : DmsException(
    AuthErrorCode.PASSWORD_MISMATCH
)
