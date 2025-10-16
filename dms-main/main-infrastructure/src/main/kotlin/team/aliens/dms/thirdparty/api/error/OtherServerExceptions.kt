package team.aliens.dms.thirdparty.api.error

import team.aliens.dms.common.error.DmsException

object OtherServerBadRequestException : DmsException(
    OtherServerErrorCode.OTHER_SERVER_BAD_REQUEST
)

object OtherServerForbiddenException : DmsException(
    OtherServerErrorCode.OTHER_SERVER_FORBIDDEN
)

object OtherServerUnauthorizedException : DmsException(
    OtherServerErrorCode.OTHER_SERVER_UNAUTHORIZED
)
