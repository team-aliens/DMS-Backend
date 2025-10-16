package team.aliens.dms.domain.room.spi

import team.aliens.dms.domain.room.model.Room
import java.util.UUID

interface QueryRoomPort {

    fun queryRoomsByRoomNumbersIn(roomNumbers: List<String>, schoolId: UUID): List<Room>
}
