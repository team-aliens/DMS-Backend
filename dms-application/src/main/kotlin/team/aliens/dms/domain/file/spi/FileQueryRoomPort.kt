package team.aliens.dms.domain.file.spi

import team.aliens.dms.domain.room.model.Room
import java.util.UUID

interface FileQueryRoomPort {

    fun queryRoomsByRoomNumbersIn(roomNumbers: List<String>, schoolId: UUID): List<Room>
}
