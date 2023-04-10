package team.aliens.dms.domain.school.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SchoolErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    ANSWER_MISMATCH(ErrorStatus.UNAUTHORIZED, "Answer Mismatch", "SCHOOL-401-1"),
    SCHOOL_CODE_MISMATCH(ErrorStatus.UNAUTHORIZED, "School Code Mismatch", "SCHOOL-401-2"),
    SCHOOL_MISMATCH(ErrorStatus.UNAUTHORIZED, "School Mismatch", "SCHOOL-402-3"),

    SCHOOL_NOT_FOUND(ErrorStatus.NOT_FOUND, "School Not Found", "SCHOOL-404-1"),
    FEATURE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Feature Not Found", "SCHOOL-404-2"),

    FEATURE_NOT_AVAILABLE(ErrorStatus.FORBIDDEN, "Feature Not Available", "SCHOOL-403-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
