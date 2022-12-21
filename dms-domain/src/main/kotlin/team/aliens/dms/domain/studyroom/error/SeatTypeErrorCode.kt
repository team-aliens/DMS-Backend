package team.aliens.dms.domain.studyroom.error

import team.aliens.dms.common.error.ErrorProperty

enum class SeatTypeErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    SEAT_TYPE_ALREADY_EXISTS(409, "Seat Type Already Exists")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}