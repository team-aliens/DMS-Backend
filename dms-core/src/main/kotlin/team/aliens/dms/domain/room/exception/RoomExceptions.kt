package team.aliens.dms.domain.room.exception

import team.aliens.dms.common.error.DmsException

object RoomNotFoundException : DmsException(
    RoomErrorCode.ROOM_NOT_FOUND
)
