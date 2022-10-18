package team.aliens.dms.domain.file.error

import team.aliens.dms.global.error.ErrorProperty

enum class FileErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    INVALID_EXTENSION(400, "제한된 확장자(jpg, jpeg, png)"),

    IO_INTERRUPTED(500, "파일 입출력 처리 중단");

    override fun status(): Int = status
    override fun message(): String = message
}