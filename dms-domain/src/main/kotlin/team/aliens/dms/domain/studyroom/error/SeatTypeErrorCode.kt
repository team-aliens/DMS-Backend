package team.aliens.dms.domain.studyroom.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SeatTypeErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    SEAT_TYPE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Seat Type Not Found"),

    SEAT_TYPE_IN_USE(ErrorStatus.CONFLICT, "Seat Type In Use"),
    SEAT_TYPE_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Seat Type Already Exists")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
