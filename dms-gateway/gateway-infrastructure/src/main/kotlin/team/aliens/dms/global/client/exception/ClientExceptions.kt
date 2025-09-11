package team.aliens.dms.global.client.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.global.client.exception.error.ClientErrorCode

object MainServiceUnavailableException : DmsException(
    ClientErrorCode.MAIN_SERVICE_UNAVAILABLE
)

object MainServiceConnectionException : DmsException(
    ClientErrorCode.MAIN_SERVICE_CONNECTION_FAILED
)

object MainServiceTimeoutException : DmsException(
    ClientErrorCode.MAIN_SERVICE_TIMEOUT
)

object PassportRetrievalException : DmsException(
    ClientErrorCode.PASSPORT_RETRIEVAL_FAILED
)

object MainServerInternalException : DmsException(
    ClientErrorCode.MAIN_SERVICE_INTERNAL_ERROR
)
