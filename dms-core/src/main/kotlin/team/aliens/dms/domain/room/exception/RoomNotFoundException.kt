package team.aliens.dms.domain.room.exception

import team.aliens.dms.common.error.DmsException
import team.aliens.dms.domain.room.exception.error.RoomErrorCode

object RoomNotFoundException : DmsException(
    RoomErrorCode.ROOM_NOT_FOUND
)
