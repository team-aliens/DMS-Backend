package team.aliens.dms.thirdparty.api.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.thirdparty.api.error.OtherServerErrorCode

object OtherServerBadRequestException : DmsException(
    OtherServerErrorCode.OTHER_SERVER_BAD_REQUEST
)
