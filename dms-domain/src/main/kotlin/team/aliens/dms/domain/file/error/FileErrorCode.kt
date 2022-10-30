package team.aliens.dms.domain.file.error

import team.aliens.dms.common.error.ErrorProperty

enum class FileErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    INVALID_EXTENSION(400, "Allowed Extension : jpg(JPG), jpeg(JPEG), png(PNG), heic(HEIC)"),

    IO_INTERRUPTED(500, "Interrupted File IO")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}