package team.aliens.dms.domain.room.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.room.spi.CommandRoomPort
import team.aliens.dms.domain.room.spi.QueryRoomPort
import java.util.UUID

@Service
class CommandRoomServiceImpl(
    private val commandRoomPort: CommandRoomPort,
    private val queryRoomPort: QueryRoomPort
) : CommandRoomService {

    override fun saveNotExistsRooms(roomNumbers: List<String>, schoolId: UUID): Map<String, Room> {
        val roomMap = queryRoomPort.queryRoomsByRoomNumbersIn(roomNumbers, schoolId)
            .associateBy { it.number }
            .toMutableMap()

        val notExistsRooms = roomNumbers.mapNotNull { roomNumber ->
            if (!roomMap.containsKey(roomNumber)) {
                Room(
                    number = roomNumber,
                    schoolId = schoolId
                ).apply {
                    roomMap[roomNumber] = this
                }
            } else { null }
        }

        commandRoomPort.saveRooms(notExistsRooms)
            .map { roomMap[it.number] = it }

        return roomMap
    }
}
