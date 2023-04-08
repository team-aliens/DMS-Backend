package team.aliens.dms.domain.room.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class RoomErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    ROOM_NOT_FOUND(ErrorStatus.NOT_FOUND, "Room Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
