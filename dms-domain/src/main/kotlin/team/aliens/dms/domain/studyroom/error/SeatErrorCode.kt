package team.aliens.dms.domain.studyroom.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SeatErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    SEAT_CAN_NOT_APPLY(ErrorStatus.FORBIDDEN, "Seat Can Not Apply"),

    SEAT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Seat Not Found"),
    APPLIED_SEAT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Applied Seat Not Found"),

    SEAT_ALREADY_APPLIED(ErrorStatus.CONFLICT, "Seat Already Applied")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
