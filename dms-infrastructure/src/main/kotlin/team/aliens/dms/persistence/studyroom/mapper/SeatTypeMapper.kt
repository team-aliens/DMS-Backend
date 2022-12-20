package team.aliens.dms.persistence.studyroom.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.model.SeatType
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.studyroom.entity.SeatTypeJpaEntity

@Component
class SeatTypeMapper : GenericMapper<SeatType, SeatTypeJpaEntity>{

    override fun toDomain(entity: SeatTypeJpaEntity?): SeatType? {
        return entity?.let {
            SeatType(
                id = it.id!!,
                name = it.name,
                color = it.color
            )
        }
    }

    override fun toEntity(domain: SeatType): SeatTypeJpaEntity {
        return SeatTypeJpaEntity(
            id = domain.id,
            name = domain.name,
            color = domain.color
        )
    }
}