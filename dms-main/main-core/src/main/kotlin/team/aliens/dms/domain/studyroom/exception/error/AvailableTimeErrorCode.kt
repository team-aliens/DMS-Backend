package team.aliens.dms.domain.studyroom.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class AvailableTimeErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    AVAILABLE_TIME_NOT_FOUND(ErrorStatus.NOT_FOUND, "Study Room Available Time Not Found", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "STUDY-ROOM-$status-$sequence"
}
