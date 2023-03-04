package team.aliens.dms.domain.file.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class FileErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    INVALID_EXTENSION(ErrorStatus.BAD_REQUEST, "Allowed Extension : jpg(JPG), jpeg(JPEG), png(PNG), heic(HEIC)"),
    BAD_EXCEL_FORMAT(ErrorStatus.BAD_REQUEST, "Bad Excel Format"),
    IO_INTERRUPTED(ErrorStatus.INTERNAL_SERVER_ERROR, "Interrupted File IO")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
