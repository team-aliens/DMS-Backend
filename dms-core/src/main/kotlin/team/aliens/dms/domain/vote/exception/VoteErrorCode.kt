package team.aliens.dms.domain.vote.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class VoteErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    STUDENT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Student Not Found", 1),
    STUDENT_ID_NOT_FOUND(ErrorStatus.BAD_REQUEST, "Student ID Not Found", 2)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "Vote-$status-$sequence"
}
