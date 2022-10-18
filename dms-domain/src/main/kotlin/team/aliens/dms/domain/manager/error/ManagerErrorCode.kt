package team.aliens.dms.domain.manager.error

import team.aliens.dms.global.error.ErrorProperty

enum class ManagerErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    ANSWER_NOT_MATCHED(401, "Answer Not Matched");

    override fun status(): Int = status
    override fun message(): String = message
}