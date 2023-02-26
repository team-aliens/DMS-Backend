package team.aliens.dms.domain.user.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.user.error.UserErrorCode

object UserAccountIdExistsException : DmsException(
    UserErrorCode.USER_ACCOUNT_ID_EXISTS
)
