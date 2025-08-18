package team.aliens.dms.domain.student.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class StudentErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    SEX_MISMATCH(ErrorStatus.BAD_REQUEST, "Sex Mismatch", 1),

    STUDENT_INFO_MISMATCH(ErrorStatus.UNAUTHORIZED, "Student Info Mismatch", 1),

    STUDENT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Student Not Found", 1),
    STUDENT_UPDATE_INFO_NOT_FOUND(ErrorStatus.NOT_FOUND, "등록되지 않았거나 정보가 일치하지 않는 학생이 존재합니다. (%s 및 %s명)", 2),

    STUDENT_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Student Already Exists", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "STUDENT-$status-$sequence"
}
