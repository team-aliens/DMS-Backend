package team.aliens.dms.persistence.room.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.room.entity.RoomJpaEntity
import team.aliens.dms.persistence.room.entity.RoomJpaEntityId
import team.aliens.dms.persistence.school.repository.SchoolRepository

@Component
class RoomMapper(
    private val schoolRepository: SchoolRepository
) : GenericMapper<Room, RoomJpaEntity> {

    override fun toDomain(e: RoomJpaEntity): Room {
        return Room(
            roomNumber = e.id.roomNumber,
            schoolId = e.id.schoolId
        )
    }

    override fun toEntity(d: Room): RoomJpaEntity {
        val school = schoolRepository.findByIdOrNull(d.schoolId) ?: throw RuntimeException()

        return RoomJpaEntity(
            id = RoomJpaEntityId(d.roomNumber, d.schoolId),
            schoolJpaEntity = school
        )
    }
}