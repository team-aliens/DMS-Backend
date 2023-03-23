package team.aliens.dms.domain.student.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class StudentErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    SEX_MISMATCH(ErrorStatus.BAD_REQUEST, "Sex Mismatch"),

    STUDENT_INFO_MISMATCH(ErrorStatus.UNAUTHORIZED, "Student Info Mismatch"),

    STUDENT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Student Not Found"),

    STUDENT_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Student Already Exists")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
