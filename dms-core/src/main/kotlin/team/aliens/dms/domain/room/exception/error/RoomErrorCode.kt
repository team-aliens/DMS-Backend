package team.aliens.dms.domain.room.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class RoomErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    ROOM_NOT_FOUND(ErrorStatus.NOT_FOUND, "Room Not Found", "ROOM-404-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
