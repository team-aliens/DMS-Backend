package team.aliens.dms.domain.room.service

import team.aliens.dms.domain.room.model.Room
import java.util.UUID

interface CommandRoomService {
    fun saveNotExistsRooms(roomNumbers: List<String>, schoolId: UUID): Map<String, Room>
}
