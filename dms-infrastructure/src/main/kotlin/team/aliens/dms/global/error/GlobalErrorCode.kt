package team.aliens.dms.global.error

enum class GlobalErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    SEND_EMAIL_REJECTED(400, "Send Email Rejected"),
    BAD_REQUEST(404, "Bad Request"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    override fun status(): Int = status
    override fun message(): String = message
}