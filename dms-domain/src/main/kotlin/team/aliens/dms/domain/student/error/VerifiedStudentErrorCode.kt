package team.aliens.dms.domain.student.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class VerifiedStudentErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    VERIFIED_STUDENT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Unverified Student")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
