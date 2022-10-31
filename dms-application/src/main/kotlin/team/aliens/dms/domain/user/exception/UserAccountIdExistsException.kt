package team.aliens.dms.domain.user.exception

import team.aliens.dms.domain.user.error.UserErrorCode
import team.aliens.dms.common.error.DmsException

object UserAccountIdExistsException : DmsException(
    UserErrorCode.USER_ACCOUNT_ID_EXISTS
)