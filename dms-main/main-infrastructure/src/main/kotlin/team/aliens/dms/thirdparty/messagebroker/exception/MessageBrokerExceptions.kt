package team.aliens.dms.thirdparty.messagebroker.exception

import team.aliens.dms.common.error.DmsException

object UnknownMessageException : DmsException(
    MessageBrokerErrorCode.UNKNOWN_MESSAGE_ERROR
)
