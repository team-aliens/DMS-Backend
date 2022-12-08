package team.aliens.dms.persistence.room.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.room.model.Room
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.room.entity.RoomJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class RoomMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<Room, RoomJpaEntity> {

    override fun toDomain(entity: RoomJpaEntity?): Room? {
        return entity?.let {
            Room(
                id = it.id!!,
                number = it.number,
                schoolId = it.school!!.id!!
            )
        }
    }

    override fun toEntity(domain: Room): RoomJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return RoomJpaEntity(
            id = domain.schoolId,
            school = school,
            number = domain.number
        )
    }
}