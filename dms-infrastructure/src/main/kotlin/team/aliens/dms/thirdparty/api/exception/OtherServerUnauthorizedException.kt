package team.aliens.dms.thirdparty.api.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.thirdparty.api.error.OtherServerErrorCode

object OtherServerUnauthorizedException : DmsException(
    OtherServerErrorCode.OTHER_SERVER_UNAUTHORIZED
)
