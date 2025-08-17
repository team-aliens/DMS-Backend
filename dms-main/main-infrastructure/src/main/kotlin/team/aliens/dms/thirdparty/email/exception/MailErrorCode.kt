package team.aliens.dms.thirdparty.email.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class MailErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    SEND_EMAIL_REJECTED(ErrorStatus.BAD_REQUEST, "Send Email Rejected", 1),
    SIMPLE_EMAIL_SERVICE(ErrorStatus.BAD_REQUEST, "Simple Email Service", 2)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "MAIL-$status-$sequence"
}
