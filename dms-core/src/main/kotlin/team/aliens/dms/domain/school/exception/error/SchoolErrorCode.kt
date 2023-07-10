package team.aliens.dms.domain.school.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SchoolErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    ANSWER_MISMATCH(ErrorStatus.UNAUTHORIZED, "Answer Mismatch", 1),
    SCHOOL_CODE_MISMATCH(ErrorStatus.UNAUTHORIZED, "School Code Mismatch", 2),
    SCHOOL_MISMATCH(ErrorStatus.UNAUTHORIZED, "School Mismatch", 3),

    SCHOOL_NOT_FOUND(ErrorStatus.NOT_FOUND, "School Not Found", 1),
    FEATURE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Feature Not Found", 2),
    APPLICATION_AVAILABLE_TIME_NOT_FOUND(ErrorStatus.NOT_FOUND, "Application Available Time Not Found", 3),

    FEATURE_NOT_AVAILABLE(ErrorStatus.FORBIDDEN, "Feature Not Available", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "SCHOOL-$status-$sequence"
}
