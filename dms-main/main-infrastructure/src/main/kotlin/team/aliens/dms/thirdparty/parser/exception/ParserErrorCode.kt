package team.aliens.dms.thirdparty.parser.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class ParserErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    EXTENSION_MISMATCH(ErrorStatus.UNAUTHORIZED, "File Extension Mismatch", 1),

    BAD_EXCEL_FORMAT(ErrorStatus.BAD_REQUEST, "%s행 등 %s개 행의 데이터 형식이 잘못되었습니다.", 1),
    INVALID_FILE(ErrorStatus.BAD_REQUEST, "Invalid File", 2),
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "PARSER-$status-$sequence"
}
