package team.aliens.dms.persistence.room

import java.util.UUID
import org.springframework.stereotype.Component
import team.aliens.dms.domain.room.spi.RoomPort
import team.aliens.dms.persistence.room.mapper.RoomMapper
import team.aliens.dms.persistence.room.repository.RoomJpaRepository

@Component
class RoomPersistenceAdapter(
    private val roomMapper: RoomMapper,
    private val roomRepository: RoomJpaRepository
) : RoomPort {

    override fun queryRoomBySchoolIdAndNumber(schoolId: UUID, number: Int) = roomMapper.toDomain(
        roomRepository.findBySchoolIdAndNumber(
            schoolId = schoolId,
            number = number
        )
    )
}