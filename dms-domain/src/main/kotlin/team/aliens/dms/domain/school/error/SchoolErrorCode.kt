package team.aliens.dms.domain.school.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SchoolErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    ANSWER_MISMATCH(ErrorStatus.UNAUTHORIZED, "Answer Mismatch"),
    SCHOOL_CODE_MISMATCH(ErrorStatus.UNAUTHORIZED, "School Code Mismatch"),
    SCHOOL_MISMATCH(ErrorStatus.UNAUTHORIZED, "School Mismatch"),

    SCHOOL_NOT_FOUND(ErrorStatus.NOT_FOUND, "School Not Found"),
    FEATURE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Feature Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
