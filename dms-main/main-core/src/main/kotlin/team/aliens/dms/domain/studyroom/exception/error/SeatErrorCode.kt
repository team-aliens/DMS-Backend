package team.aliens.dms.domain.studyroom.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SeatErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    SEAT_CAN_NOT_APPLY(ErrorStatus.FORBIDDEN, "Seat Can Not Apply", 1),

    SEAT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Seat Not Found", 2),
    APPLIED_SEAT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Applied Seat Not Found", 3),

    SEAT_ALREADY_APPLIED(ErrorStatus.CONFLICT, "Seat Already Applied", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "STUDY-ROOM-$status-$sequence"
}
