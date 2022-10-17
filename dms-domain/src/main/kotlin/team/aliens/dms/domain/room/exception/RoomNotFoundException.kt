package team.aliens.dms.domain.room.exception

import team.aliens.dms.domain.room.error.RoomErrorCode
import team.aliens.dms.global.error.DmsException

object RoomNotFoundException : DmsException(
    RoomErrorCode.ROOM_NOT_FOUND
)