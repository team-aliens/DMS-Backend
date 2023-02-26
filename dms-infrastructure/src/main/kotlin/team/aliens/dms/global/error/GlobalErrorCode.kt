package team.aliens.dms.global.error

import team.aliens.dms.common.error.ErrorProperty

enum class GlobalErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    SEND_EMAIL_REJECTED(400, "Send Email Rejected"),
    SIMPLE_EMAIL_SERVICE(400, "Simple Email Service"),
    BAD_REQUEST(400, "Bad Request"),
    INVALID_FILE(400, "Invalid File"),

    SEX_MISMATCH(401, "Sex Mismatch"),
    EXTENSION_MISMATCH(401, "File Extension Mismatch"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
