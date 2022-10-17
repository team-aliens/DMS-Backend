package team.aliens.dms.persistence.room.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.room.entity.RoomJpaEntity
import team.aliens.dms.persistence.room.entity.RoomJpaEntityId
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class RoomMapper(
    private val schoolJpaRepository: SchoolJpaRepository
) : GenericMapper<Room, RoomJpaEntity> {

    override fun toDomain(entity: RoomJpaEntity?): Room? {
        return entity?.let {
            Room(
                roomNumber = it.id.roomNumber,
                schoolId = it.id.schoolId
            )
        }
    }

    override fun toEntity(domain: Room): RoomJpaEntity {
        val school = schoolJpaRepository.findByIdOrNull(domain.schoolId) ?: throw SchoolNotFoundException

        return RoomJpaEntity(
            id = RoomJpaEntityId(domain.roomNumber, domain.schoolId),
            school = school
        )
    }
}