package team.aliens.dms.domain.auth.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.auth.error.AuthErrorCode

object EmailAlreadyCertifiedException : DmsException(
    AuthErrorCode.EMAIL_ALREADY_CERTIFIED
)
