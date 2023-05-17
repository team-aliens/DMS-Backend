package team.aliens.dms.global.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class GlobalErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    SEND_EMAIL_REJECTED(ErrorStatus.BAD_REQUEST, "Send Email Rejected", 1),
    SIMPLE_EMAIL_SERVICE(ErrorStatus.BAD_REQUEST, "Simple Email Service", 2),
    BAD_REQUEST(ErrorStatus.BAD_REQUEST, "Bad Request", 3),
    INVALID_FILE(ErrorStatus.BAD_REQUEST, "Invalid File", 4),
    BAD_EXCEL_FORMAT(ErrorStatus.BAD_REQUEST, "%s행 등 %s개 행의 데이터 형식이 잘못되었습니다.", 5),

    EXTENSION_MISMATCH(ErrorStatus.UNAUTHORIZED, "File Extension Mismatch", 1),

    INTERNAL_SERVER_ERROR(ErrorStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "GLOBAL-$status-$sequence"
}
