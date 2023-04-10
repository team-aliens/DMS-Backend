package team.aliens.dms.domain.tag.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class TagErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    TAG_NOT_FOUND(ErrorStatus.NOT_FOUND, "Tag Not Found", "TAG-404-1"),
    TAG_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Tag Already Exists", "TAG-409-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
