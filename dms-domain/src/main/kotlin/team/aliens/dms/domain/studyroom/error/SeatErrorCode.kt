package team.aliens.dms.domain.studyroom.error

import team.aliens.dms.common.error.ErrorProperty

enum class SeatErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    SEAT_NOT_FOUND(404, "Seat Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}