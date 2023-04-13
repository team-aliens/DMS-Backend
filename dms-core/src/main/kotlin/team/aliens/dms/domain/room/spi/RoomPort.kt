package team.aliens.dms.domain.room.spi

import team.aliens.dms.domain.file.spi.FileCommandRoomPort
import team.aliens.dms.domain.file.spi.FileQueryRoomPort
import team.aliens.dms.domain.student.spi.StudentQueryRoomPort

interface RoomPort :
    FileQueryRoomPort,
    FileCommandRoomPort
