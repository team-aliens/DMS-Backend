package team.aliens.dms.global.error

import team.aliens.dms.common.error.ErrorProperty

enum class GlobalErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    SEND_EMAIL_REJECTED(400, "Send Email Rejected", 1),
    SIMPLE_EMAIL_SERVICE(400, "Simple Email Service", 2),
    BAD_REQUEST(400, "Bad Request", 3),
    INVALID_FILE(400, "Invalid File", 4),

    EXTENSION_MISMATCH(401, "File Extension Mismatch", 1),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "GLOBAL-$status-$sequence"
}
