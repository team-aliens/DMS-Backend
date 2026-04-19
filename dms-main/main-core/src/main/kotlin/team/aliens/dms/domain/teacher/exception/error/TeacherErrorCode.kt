package team.aliens.dms.domain.teacher.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class TeacherErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    TEACHER_NOT_FOUND(ErrorStatus.NOT_FOUND, "Teacher Not Found", 1);

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "TEACHER-$status-$sequence"
}
