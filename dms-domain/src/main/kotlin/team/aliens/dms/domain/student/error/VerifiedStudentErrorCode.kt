package team.aliens.dms.domain.student.error

import team.aliens.dms.common.error.ErrorProperty

enum class VerifiedStudentErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    VERIFIED_STUDENT_NOT_FOUND(404, "Unverified Student")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}