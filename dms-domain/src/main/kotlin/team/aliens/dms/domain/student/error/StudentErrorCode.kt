package team.aliens.dms.domain.student.error

import team.aliens.dms.common.error.ErrorProperty

enum class StudentErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    STUDENT_INFO_MISMATCH(401, "Student Info Mismatch"),

    STUDENT_NOT_FOUND(404, "Student Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}