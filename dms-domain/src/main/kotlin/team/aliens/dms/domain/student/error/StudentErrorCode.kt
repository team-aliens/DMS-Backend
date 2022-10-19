package team.aliens.dms.domain.student.error

import team.aliens.dms.global.error.ErrorProperty

enum class StudentErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    STUDENT_NOT_FOUND(404, "Student Not Found"),
    STUDENT_INFO_NOT_MATCHED(401, "Student Info Not Matched")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}