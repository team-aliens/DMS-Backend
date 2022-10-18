package team.aliens.dms.domain.student.error

import team.aliens.dms.global.error.ErrorProperty

enum class StudentErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    STUDENT_NOT_FOUND(404, "Student Not Found"),
    STUDENT_EMAIL_EXISTS(409, "Student Email Exists")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}