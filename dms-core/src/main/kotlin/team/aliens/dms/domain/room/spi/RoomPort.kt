package team.aliens.dms.domain.room.spi

import team.aliens.dms.domain.file.spi.FileCommandRoomPort
import team.aliens.dms.domain.file.spi.FileQueryRoomPort

interface RoomPort :
    FileQueryRoomPort,
    FileCommandRoomPort
