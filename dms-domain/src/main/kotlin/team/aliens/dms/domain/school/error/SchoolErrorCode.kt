package team.aliens.dms.domain.school.error

import team.aliens.dms.global.error.ErrorProperty

enum class SchoolErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    ANSWER_NOT_MATCHED(401, "Answer Not Matched"),
    CODE_NOT_MATCHED(401, "Code Not Matched"),
    SCHOOL_NOT_FOUND(404, "School Not Found");

    override fun status(): Int = status
    override fun message(): String = message
}