package team.aliens.dms.domain.manager.exception

import team.aliens.dms.domain.auth.error.AuthErrorCode
import team.aliens.dms.global.error.DmsException

object AuthCodeNotMatchedException : DmsException(
    AuthErrorCode.AUTH_CODE_NOT_MATCHED
)