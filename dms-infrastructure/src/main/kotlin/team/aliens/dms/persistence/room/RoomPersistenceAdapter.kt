package team.aliens.dms.persistence.room

import org.springframework.stereotype.Component
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.room.spi.RoomPort
import team.aliens.dms.persistence.room.mapper.RoomMapper
import team.aliens.dms.persistence.room.repository.RoomJpaRepository
import java.util.UUID

@Component
class RoomPersistenceAdapter(
    private val roomMapper: RoomMapper,
    private val roomRepository: RoomJpaRepository
) : RoomPort {

    override fun queryRoomBySchoolIdAndNumber(schoolId: UUID, number: String) = roomMapper.toDomain(
        roomRepository.findBySchoolIdAndNumber(
            schoolId = schoolId,
            number = number
        )
    )

    override fun queryRoomsByRoomNumbersIn(roomNumbers: List<String>, schoolId: UUID): List<Room> {
        return roomRepository.findAllByNumberIsInAndSchoolId(
            numbers = roomNumbers,
            schoolId = schoolId
        ).mapNotNull(roomMapper::toDomain)
    }

    override fun saveRooms(rooms: List<Room>) =
        roomRepository.saveAll(
            rooms.map {  roomMapper.toEntity(it) }
        ).map {
            roomMapper.toDomain(it)!!
        }
}
