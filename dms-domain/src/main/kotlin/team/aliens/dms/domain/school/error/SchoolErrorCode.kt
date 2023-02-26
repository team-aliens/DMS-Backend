package team.aliens.dms.domain.school.error

import team.aliens.dms.common.error.ErrorProperty

enum class SchoolErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    ANSWER_MISMATCH(401, "Answer Mismatch"),
    SCHOOL_CODE_MISMATCH(401, "School Code Mismatch"),
    SCHOOL_MISMATCH(401, "School Mismatch"),

    SCHOOL_NOT_FOUND(404, "School Not Found"),
    FEATURE_NOT_FOUND(404, "Feature Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
