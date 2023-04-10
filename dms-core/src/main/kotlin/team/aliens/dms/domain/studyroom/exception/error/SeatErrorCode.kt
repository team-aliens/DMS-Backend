package team.aliens.dms.domain.studyroom.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SeatErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    SEAT_CAN_NOT_APPLY(ErrorStatus.FORBIDDEN, "Seat Can Not Apply", "STUDY-ROOM-403-1"),

    SEAT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Seat Not Found", "STUDY-ROOM-404-2"),
    APPLIED_SEAT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Applied Seat Not Found", "STUDY-ROOM-404-3"),

    SEAT_ALREADY_APPLIED(ErrorStatus.CONFLICT, "Seat Already Applied", "STUDY-ROOM-409-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
