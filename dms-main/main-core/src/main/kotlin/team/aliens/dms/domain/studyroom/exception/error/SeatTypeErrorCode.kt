package team.aliens.dms.domain.studyroom.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class SeatTypeErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    SEAT_TYPE_NOT_FOUND(ErrorStatus.NOT_FOUND, "Seat Type Not Found", 4),

    SEAT_TYPE_IN_USE(ErrorStatus.CONFLICT, "Seat Type In Use", 2),
    SEAT_TYPE_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Seat Type Already Exists", 3)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "STUDY-ROOM-$status-$sequence"
}
