package team.aliens.dms.domain.studyroom.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SeatTypeErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    SEAT_TYPE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Seat Type Not Found", "STUDY-ROOM-404-4"),

    SEAT_TYPE_IN_USE(ErrorStatus.CONFLICT, "Seat Type In Use", "STUDY-ROOM-409-2"),
    SEAT_TYPE_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Seat Type Already Exists", "STUDY-ROOM-409-3")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
