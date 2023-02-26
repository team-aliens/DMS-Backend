package team.aliens.dms.domain.studyroom.error

import team.aliens.dms.common.error.ErrorProperty

enum class SeatErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    SEAT_CAN_NOT_APPLY(403, "Seat Can Not Apply"),

    SEAT_NOT_FOUND(404, "Seat Not Found"),
    APPLIED_SEAT_NOT_FOUND(404, "Applied Seat Not Found"),

    SEAT_ALREADY_APPLIED(409, "Seat Already Applied")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
