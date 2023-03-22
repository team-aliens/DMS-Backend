package team.aliens.dms.domain.tag.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class TagErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    TAG_NOT_FOUND(ErrorStatus.NOT_FOUND, "Tag Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}