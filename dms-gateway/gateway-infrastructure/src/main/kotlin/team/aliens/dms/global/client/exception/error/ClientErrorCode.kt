package team.aliens.dms.global.client.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class ClientErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    MAIN_SERVICE_UNAVAILABLE(ErrorStatus.SERVICE_UNAVAILABLE, "Main service is temporarily unavailable", 1),
    MAIN_SERVICE_CONNECTION_FAILED(ErrorStatus.SERVICE_UNAVAILABLE, "Failed to connect to main service", 2),

    MAIN_SERVICE_TIMEOUT(ErrorStatus.GATEWAY_TIMEOUT, "Main service response timed out", 1),

    PASSPORT_RETRIEVAL_FAILED(ErrorStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred during main service integration", 1),
    MAIN_SERVICE_INTERNAL_ERROR(ErrorStatus.INTERNAL_SERVER_ERROR, "Main server internal error", 2),
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "CLIENT-$status-$sequence"
}
