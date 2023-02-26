package team.aliens.dms.domain.room.error

import team.aliens.dms.common.error.ErrorProperty

enum class RoomErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    ROOM_NOT_FOUND(404, "Room Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
