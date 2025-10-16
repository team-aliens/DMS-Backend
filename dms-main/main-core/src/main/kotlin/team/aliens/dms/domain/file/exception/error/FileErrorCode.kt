package team.aliens.dms.domain.file.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class FileErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    INVALID_EXTENSION(ErrorStatus.BAD_REQUEST, "Allowed Extension : jpg(JPG), jpeg(JPEG), png(PNG), heic(HEIC)", 1),
    BAD_EXCEL_FORMAT(ErrorStatus.BAD_REQUEST, "Bad Excel Format", 2),
    IO_INTERRUPTED(ErrorStatus.INTERNAL_SERVER_ERROR, "Interrupted File IO", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "FILE-$status-$sequence"
}
